package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.dao.*;
import com.example.auctionapp.entity.*;
import com.example.auctionapp.service.IAccountService;
import com.example.auctionapp.service.IGoodsOrderService;
import com.example.auctionapp.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品订单 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@Service
public class GoodsOrderServiceImpl implements IGoodsOrderService {


    @Resource
    GoodsOrderMapper goodsOrderMapper;

    @Resource
    TransLogMapper transLogMapper;

    @Resource
    AccountMapper accountMapper;

    @Resource
    IAccountService iAccountService;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    AgentMapper agentMapper;

    @Resource
    ProfitModulMapper profitModulMapper;

    @Resource
    AuctionGoodsMapper auctionGoodsMapper;

    @Override
    public Page<Map> selectCustomerOrderInfo(Page page, Integer customerId, Integer status) {
        List<Map> auctionGoods = goodsOrderMapper.selectCustomerOrderInfo(page, customerId, status);
        page.setRecords(auctionGoods);
        return page;
    }

    /**
     * 订单余额支付支付
     *
     * @param transLog
     */
    @Override
    @Transactional
    public Result orderPayByBalance(TransLog transLog) {
        //获取订单信息
        GoodsOrder goodsOrder = goodsOrderMapper.selectByOrderNumber(transLog.getOrderNumber()); //订单编号缓存到remark中
        //校验订单状态
        if (goodsOrder.getStatus() != 0) {
            log.error("该订单状态[" + goodsOrder.getStatus() + "]有误，请确认订单！");
            return ResultGenerator.genSuccessMEssageResult("该订单状态有误，请确认订单！");
        }
        //余额校验
        Account account = new Account();
        account.setSubjectId(transLog.getSubject());
        account.setType(ProjectConstant.ACCOUNT_TYPE_CUSTOMER);
        account = accountMapper.selectBySubjectIdAndTpye(account);
        if (account.getBalance().compareTo(transLog.getPayAmount()) < 0) {
            log.error("余额不足，当前余额为:" + account.getBalance() + "需要支付金额为:" + transLog.getPayAmount());
            return ResultGenerator.genSuccessMEssageResult("您的余额不足，请充值后再次竞拍");
        }
        //if (!iAccountService.verifyPayPass(account, transLog.getPassword())) {
        //  log.error("密码错误，账户id为：" + account.getId());
        //  return ResultGenerator.genSuccessMEssageResult("你输入的密码不正确，请再次尝试");
        //}
        //余额消费 balance 减去实际的支付金额
        account.setBalance(account.getBalance().subtract(transLog.getPayAmount()));
        accountMapper.updateBalanceById(account);

        //更新订单信息
        goodsOrder.setStatus(3);
        goodsOrder.setAddressId(transLog.getAddressId());
        goodsOrder.setTailAmountChannel(transLog.getChannel());
        goodsOrderMapper.updateById(goodsOrder);

        //记录交易流水表
        transLog.setOrderNumber(goodsOrder.getOrderNumber());
        transLog.setAccountType(account.getType());
        transLog.setAccountId(account.getId());
        transLog.setStatus(1);
        log.info("table trans_log data:" + transLog);
        transLogMapper.insert(transLog);

        //订单分润处理
        shareProfitForOrder(goodsOrder);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 订单第三方支付
     *
     * @param transLog
     */
    @Override
    @Transactional
    public boolean orderPayByThird(TransLog transLog) {
        //获取订单信息
        GoodsOrder goodsOrder = goodsOrderMapper.selectByOrderNumber(transLog.getRemark()); //订单编号缓存到remark中
        if (goodsOrder.getStatus() != 0) {
            log.error("该订单状态[" + goodsOrder.getStatus() + "]有误，请确认订单！");
            return false;
        }
        //更新订单信息
        goodsOrder.setStatus(3);
        goodsOrder.setAddressId(transLog.getAddressId());
        goodsOrder.setTailAmountChannel(transLog.getChannel());
        goodsOrderMapper.updateById(goodsOrder);

        //跟新交易信息为成功
        transLogMapper.updateSucessById(transLog);
        //订单分润处理
        shareProfitForOrder(goodsOrder);
        return true;
    }

    @Override
    public boolean update(GoodsOrder goodsOrder) {
        return goodsOrderMapper.updateById(goodsOrder) > 1;
    }

    /**
     * 订单分润处理
     *
     * @param goodsOrder
     */
    public void shareProfitForOrder(GoodsOrder goodsOrder) {
        BigDecimal bigValue100 = new BigDecimal(100);
        BigDecimal profitValue = new BigDecimal(100);

        //获取订单用户信息
        Customer customer = customerMapper.selectById(goodsOrder.getCustomerId());
        if (customer.getAgentId() == null || customer.getAgentId() == 0) {
            log.info("该订单[" + goodsOrder.getOrderNumber() + "]没有可分润的代理");
            return;
        }
        //获取订单拍品信息
        AuctionGoods auctionGoods = auctionGoodsMapper.selectById(goodsOrder.getGoodsId());
        //获取代理信息
        Agent agent = agentMapper.selectById(customer.getAgentId());
        if (agent.getProportionType() == 1) {
            //如果分润类型为默认比例，则需要在分润模板表中查找默认比例
            ProfitModul profitModul = profitModulMapper.selectByGoodsPrice(auctionGoods.getGoodsPrice());
            //将模板比例赋值给个固定比例
            agent.setProportion(profitModul.getProportion());
            agent.setParentProportion(profitModul.getParentProportion());
        }

        //应分润金额
        profitValue = goodsOrder.getActualPayment().multiply(agent.getProportion().divide(bigValue100));

        //查询所属代理账号信息
        Account account = new Account();
        account.setType(ProjectConstant.ACCOUNT_YYPE_AGENT);
        TransLog transLog = new TransLog();
        account.setSubjectId(agent.getId());
        account = accountMapper.selectBySubjectIdAndTpye(account);
        //组装交易信息
        transLog.setOrderNumber(ProjectConstant.SHARE_ORDER + DateTimeUtil.getNowInSS() + agent.getId());
        transLog.setGoodsId(auctionGoods.getId());
        transLog.setSubject(agent.getId());
        transLog.setAccountType(account.getType());
        transLog.setAccountId(account.getId());
        transLog.setTransType(9);
        transLog.setTransSign(1);
        transLog.setStatus(1);

        //一级代理分润
        if (agent.getParentId() == null) {
            //直接分发应分润金额给本代理
            account.setBalance(account.getBalance().add(profitValue));
            transLog.setAmount(profitValue);
            transLog.setPayAmount(profitValue);
            accountMapper.updateBalanceById(account);
            transLogMapper.insert(transLog);
        } else if (agent.getParentId() > 0) {   //二级代理分润
            //父及代理分润
            BigDecimal profitValue1 = profitValue.multiply(agent.getProportion().divide(bigValue100));
            //二级代理（本代理）分润
            BigDecimal profitValue2 = profitValue.subtract(profitValue1);
            //分发二级代理（本代理）利润
            account.setBalance(account.getBalance().add(profitValue2));
            transLog.setAmount(profitValue2);
            transLog.setPayAmount(profitValue2);
            accountMapper.updateBalanceById(account);
            transLogMapper.insert(transLog);

            //父及代理分润
            //获取父及代理账户
            account.setSubjectId(agent.getParentId());
            account = accountMapper.selectBySubjectIdAndTpye(account);
            //分发父级代理利润
            account.setBalance(account.getBalance().add(profitValue1));
            transLog.setOrderNumber(ProjectConstant.SHARE_ORDER + DateTimeUtil.getNowInSS() + agent.getParentId());
            transLog.setSubject(agent.getParentId());
            transLog.setAmount(profitValue1);
            transLog.setPayAmount(profitValue1);
            accountMapper.updateBalanceById(account);
            transLogMapper.insert(transLog);
        }
    }

    /**
     * 根据商品id查询出商品
     *
     * @param goodsId 商品id
     * @return
     */
    @Override
    public GoodsOrder getGoodsOrderByGoodsId(Integer goodsId) {

        return goodsOrderMapper.selectOne(new QueryWrapper<GoodsOrder>().eq("goods_id", goodsId));
    }
}
