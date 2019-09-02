package com.example.auctionapp.service.impl;

import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.RedisConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.dao.*;
import com.example.auctionapp.entity.*;
import com.example.auctionapp.entity.ext.MarkupRecordSummary;
import com.example.auctionapp.entity.ext.ShareProfit;
import com.example.auctionapp.exceptions.AuctionException;
import com.example.auctionapp.service.*;
import com.example.auctionapp.util.DateTimeUtil;
import com.example.auctionapp.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 拍品 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@Service
public class AuctionServiceImpl implements IAuctionService {

    @Resource
    AuctionGoodsMapper auctionGoodsMapper;

    @Resource
    FieldGoodsMapper fieldGoodsMapper;

    @Resource
    AccountMapper accountMapper;

    @Resource
    MarkupRecordMapper markupRecordMapper;

    @Resource
    MarkupRecordHisMapper markupRecordHisMapper;

    @Resource
    TransLogMapper transLogMapper;

    @Resource
    GoodsOrderMapper goodsOrderMapper;

    @Resource
    MessageMapper messageMapper;

    @Resource
    ProfitModulMapper profitModulMapper;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    IAccountService iAccountService;

    @Resource
    ICouponsService iCouponsService;

    @Resource
    ICouponManageService iCouponManageService;

    @Autowired
    private IRedisService redisService;

    /**
     * 第三方支付拍卖接口
     *
     * @param transLog
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class, timeout = 10)
    public boolean auctionByThird(TransLog transLog) {
        //获取商品id
        AuctionGoods auctionGoods = auctionGoodsMapper.selectById(transLog.getGoodsId());
        log.info("auction info:{}", auctionGoods);
        if (auctionGoods == null) {
            log.info("该商品不存在 auctionGoods.getId()={}",auctionGoods.getId());
            return false;
        }
        if (auctionGoods.getStatus() == ProjectConstant.ACCOUNT_GOOODS_STATUS_NOTSTARTED) {
            log.info("该商品尚未开拍，请选择其他商品 auctionGoods.getId()={}",auctionGoods.getId());
            return false;
        } else if (auctionGoods.getStatus() == ProjectConstant.ACCOUNT_GOOODS_STATUS_END) {
            log.info("手慢了，宝贝被其他人抢走啦~,auctionGoods.getId()={}",auctionGoods.getId());
            return false;
        }

        //是否已经被拍走
        Object  auctionGoodsLock=redisService.get(RedisConstant.RDS_AUCTION_LOCK_KEY_PREFIX+auctionGoods.getId());
        if(auctionGoodsLock !=null){
            log.info("手慢了，宝贝被其他人抢走啦~,auctionGoods.getId()={}",auctionGoods.getId());
            return false;
        }
        FieldGoods fieldGoods = fieldGoodsMapper.selectByGoodsId(transLog.getGoodsId());
        MarkupRecordSummary markupRecordSummary = markupRecordMapper.selectSummaryByGoodsId(auctionGoods.getId());
        if (markupRecordSummary == null) {
            markupRecordSummary = new MarkupRecordSummary();
            markupRecordSummary.setMaxTotalNumber(0);
        }
        log.info("markupRecordSummary info:{}", markupRecordSummary);
        MarkupRecordSummary markupRecordSummaryUserId = markupRecordMapper.selectSummaryByGoodsIdAndUserId(auctionGoods.getId(), transLog.getSubject());
        if (markupRecordSummaryUserId == null) {
            markupRecordSummaryUserId = new MarkupRecordSummary();
            markupRecordSummaryUserId.setMaxShootingTimes(0);
        }
        //账户校验
        Account account = new Account();
        account.setSubjectId(transLog.getSubject());
        account.setType(ProjectConstant.ACCOUNT_TYPE_CUSTOMER);
        account = accountMapper.selectBySubjectIdAndTpye(account);
        //优惠券处理
        if (transLog.getCoupons() != null
                && transLog.getCouponsId() != null
                && transLog.getCoupons().compareTo(new BigDecimal(0)) > 0
                && transLog.getCouponsId() > 0) {
            if (!iCouponManageService.useCoupons(transLog.getCouponsId())) {
                log.error("优惠信息有误，请确认后在拍");
                return false;
            }
        }
        //记录交易流水表
        if (fieldGoods != null) {
            transLog.setFieldId(fieldGoods.getField());
        }
        transLog.setAccountType(account.getType());
        transLog.setAccountId(account.getId());
        transLog.setUpdateTime(null);
        transLog.setStatus(1);
        log.info("table trans_log data:{}", transLog);
        transLogMapper.updateById(transLog);
        //记录加价记录表
        MarkupRecord markupRecord = new MarkupRecord();
        markupRecord.setFieldId(transLog.getFieldId());
        markupRecord.setGoodsId(transLog.getGoodsId());
        markupRecord.setCurrentBid(transLog.getCurrentBid());
        markupRecord.setTotalNumber(markupRecordSummary.getMaxTotalNumber() + 1);
        markupRecord.setShootingTimes(markupRecordSummaryUserId.getMaxShootingTimes() + 1);
        markupRecord.setUserId(transLog.getSubject());
        markupRecord.setBond(transLog.getBond());
        markupRecord.setCommission(transLog.getCommission());
        //状态设置成1-已支付
        markupRecord.setCmsStatus(1);
        markupRecord.setCoupons(transLog.getCoupons());
        markupRecord.setCouponsType(transLog.getCouponsType());
        markupRecord.setPayType(transLog.getChannel());
        markupRecord.setPayAmount(transLog.getPayAmount());
        //数据状态1-生效
        markupRecord.setStatus(1);
        markupRecord.setOrderNumber(transLog.getOrderNumber());
        log.info("table markup_record data:{}", markupRecord);
        markupRecordMapper.insert(markupRecord);
        //拍中计算
        auctionCheck(auctionGoods);

        //优惠券发放
        iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_AUCTIONGOODS, transLog.getSubject());
        return true;
    }

    /**
     * 余额拍卖接口
     *
     * @param transLog
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class, timeout = 10)
    public Result auctionByBalance(TransLog transLog) {
        AuctionGoods auctionGoods = auctionGoodsMapper.selectById(transLog.getGoodsId());
        log.info("auction info:{}", auctionGoods);
        if (auctionGoods == null) {
            return ResultGenerator.genSuccessMEssageResult("该商品不存在");
        }
        if (auctionGoods.getStatus() == ProjectConstant.ACCOUNT_GOOODS_STATUS_NOTSTARTED) {
            return ResultGenerator.genSuccessMEssageResult("该商品尚未开拍，请选择其他商品");
        } else if (auctionGoods.getStatus() == ProjectConstant.ACCOUNT_GOOODS_STATUS_END) {
            return ResultGenerator.genSuccessMEssageResult("手慢了，宝贝被其他人抢走啦~");
        }

        //是否已经被拍走
        Object  auctionGoodsLock=redisService.get(RedisConstant.RDS_AUCTION_LOCK_KEY_PREFIX+auctionGoods.getId());
        if(auctionGoodsLock !=null){
            return ResultGenerator.genSuccessMEssageResult("手慢了，宝贝被其他人抢走啦~");
        }

        FieldGoods fieldGoods = fieldGoodsMapper.selectByGoodsId(transLog.getGoodsId());

        //获取拍品的最高出价
        MarkupRecordSummary markupRecordSummary = markupRecordMapper.selectSummaryByGoodsId(auctionGoods.getId());
        //对象不存在，默认价格出价为0
        if (markupRecordSummary == null) {
            markupRecordSummary = new MarkupRecordSummary();
            markupRecordSummary.setMaxTotalNumber(0);
        }
        log.info("markupRecordSummary info:{}", markupRecordSummary);

        //拍卖汇总信息
        MarkupRecordSummary markupRecordSummaryUserId = markupRecordMapper.selectSummaryByGoodsIdAndUserId(auctionGoods.getId(), transLog.getSubject());
        if (markupRecordSummaryUserId == null) {
            markupRecordSummaryUserId = new MarkupRecordSummary();
            markupRecordSummaryUserId.setMaxShootingTimes(0);
        }
        Account account = new Account();
        account.setSubjectId(transLog.getSubject());
        account.setType(ProjectConstant.ACCOUNT_TYPE_CUSTOMER);
        account = accountMapper.selectBySubjectIdAndTpye(account);
        if (account.getBalance().compareTo(transLog.getPayAmount()) < 0) {
            log.error("余额不足，当前余额为:{} 需要支付金额为:{}", account.getBalance(), transLog.getPayAmount());
            return ResultGenerator.genSuccessMEssageResult("您的余额不足，请充值后再次竞拍");
        }
        //if (!iAccountService.verifyPayPass(account, transLog.getPassword())) {
        //  log.error("密码错误，账户id为：{}", account.getId());
        //  return ResultGenerator.genSuccessMEssageResult("你输入的密码不正确，请再次尝试");
        //}
        //优惠券处理 核销优惠券
        if (transLog.getCoupons() != null && transLog.getCouponsId() != null && transLog.getCoupons().compareTo(new BigDecimal(0)) > 0 && transLog.getCouponsId() > 0) {
            if (!iCouponManageService.useCoupons(transLog.getCouponsId())) {
                log.error("优惠信息有误，请确认后在拍");
                return ResultGenerator.genSuccessMEssageResult("优惠信息有误，请确认后在拍");
            }
        }
        //余额消费 balance 减去实际的支付金额
        account.setBalance(account.getBalance().subtract(transLog.getPayAmount()));

        //修改余额
        iAccountService.updateBalanceById(account);
        //记录交易流水表
        transLog.setOrderNumber(ProjectConstant.GOODS_AUCTION + DateTimeUtil.getNowInSS() + transLog.getSubject());
        if (fieldGoods != null) {
            transLog.setFieldId(fieldGoods.getField());
        }
        transLog.setAccountType(account.getType());
        transLog.setAccountId(account.getId());
        transLog.setStatus(1);
        log.info("table trans_log data:{}", transLog);
        transLogMapper.insert(transLog);
        MarkupRecord markupRecord = new MarkupRecord();
        markupRecord.setFieldId(transLog.getFieldId());
        markupRecord.setGoodsId(transLog.getGoodsId());
        markupRecord.setCurrentBid(transLog.getCurrentBid());
        markupRecord.setTotalNumber(markupRecordSummary.getMaxTotalNumber() + 1);
        markupRecord.setShootingTimes(markupRecordSummaryUserId.getMaxShootingTimes() + 1);
        markupRecord.setUserId(transLog.getSubject());
        markupRecord.setBond(transLog.getBond());
        markupRecord.setCommission(transLog.getCommission());
        markupRecord.setCmsStatus(1);
        markupRecord.setCoupons(transLog.getCoupons());
        markupRecord.setCouponsType(transLog.getCouponsType());
        markupRecord.setPayType(transLog.getChannel());
        markupRecord.setPayAmount(transLog.getPayAmount());
        markupRecord.setStatus(1);
        markupRecord.setOrderNumber(transLog.getOrderNumber());
        log.info("table markup_record data:{}", markupRecord);
        markupRecordMapper.insert(markupRecord);
        //拍中计算
        auctionCheck(auctionGoods);
        //优惠券发放
        iCouponsService.sendCoupons(ProjectConstant.COUPONS_SENDTYPE_AUCTIONGOODS, transLog.getSubject());
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 到期未拍出拍品处理
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class, timeout = 5)
    public void auctionExpireProcess() {
        //获取已经过去的拍品列表
        List<AuctionGoods> auctionGoodsList = auctionGoodsMapper.getExpireAuctionGoods();
        if (auctionGoodsList == null || auctionGoodsList.size() == 0) {
            return;
        }
        for (AuctionGoods auctionGoodsTmp : auctionGoodsList) {

            //先判断是否有人再参拍
            boolean lockStatus=redisService.setnx(RedisConstant.RDS_AUCTION_LOCK_KEY_PREFIX+auctionGoodsTmp.getId(),auctionGoodsTmp.getId(),5L);
            if(!lockStatus) {
                continue;
            }

            //设置拍品已拍出
            auctionGoodsTmp.setStatus(2);
            auctionGoodsTmp.setUpdateTime(null);
            auctionGoodsMapper.updateById(auctionGoodsTmp);
            //获取拍品的汇总拍卖汇总信息
            MarkupRecordSummary markupRecordSummary = markupRecordMapper.selectSummaryByGoodsId(auctionGoodsTmp.getId());
            if (markupRecordSummary == null) {
                markupRecordSummary = new MarkupRecordSummary();
                markupRecordSummary.setMaxTotalNumber(0);
                markupRecordSummary.setSumCommission(new BigDecimal(0));
                markupRecordSummary.setMaxCurrentBid(auctionGoodsTmp.getStartingPrice());
            }
            //插入机器人拍卖纪录
            String headPhoto = StringUtils.getHeadPhoto();

            MarkupRecord markupRecord = new MarkupRecord();
            //暂时缓存机器人手机号，此处是个坑
            markupRecord.setOrderNumber(StringUtils.getTelephone());
            markupRecord.setGoodsId(auctionGoodsTmp.getId());
            markupRecord.setCurrentBid(markupRecordSummary.getMaxCurrentBid().add(auctionGoodsTmp.getSingleCommission()));
            markupRecord.setTotalNumber(markupRecordSummary.getMaxTotalNumber() + 1);
            markupRecord.setShootingTimes(1);
            markupRecord.setBond(auctionGoodsTmp.getBond());
            markupRecord.setCommission(auctionGoodsTmp.getSingleCommission());
            //状态设置成1-已支付
            markupRecord.setCmsStatus(1);
            markupRecord.setPayType("balance");
            //数据状态1-生效
            markupRecord.setStatus(1);
            markupRecord.setRemark(headPhoto);
            log.info("table markup_record data:{}", markupRecord);
            markupRecordMapper.insert(markupRecord);

            //插入订单表一条记录
            GoodsOrder goodsOrder = new GoodsOrder();
            goodsOrder.setOrderNumber("LP" + DateTimeUtil.getNowInSS());
            goodsOrder.setCustomerId(0);
            goodsOrder.setGoodsId(auctionGoodsTmp.getId());
            goodsOrder.setBond(auctionGoodsTmp.getBond());
            goodsOrder.setTotalNumber(0);
            goodsOrder.setTotalNumberAll(markupRecordSummary.getMaxTotalNumber());
            goodsOrder.setReferenceCommission(new BigDecimal(0));
            goodsOrder.setReferenceCommissionAll(markupRecordSummary.getSumCommission());
            goodsOrder.setPreferentialAmount(new BigDecimal(0));
            goodsOrder.setActualPayment(new BigDecimal(0));
            //尾款金额为实际出价减去保证金
            goodsOrder.setTailAmount(new BigDecimal(0));
            //订单状态为5-已完成
            goodsOrder.setStatus(5);
            //在remark字段中存储机器人昵称
            goodsOrder.setRemark(headPhoto);
            log.info("订单信息:{}", goodsOrder);
            goodsOrderMapper.insert(goodsOrder);

            //退还未拍中客户保证金到余额
            returnBond(auctionGoodsTmp, 0);
            //对代理进行分润
            shareProfit(auctionGoodsTmp);
            //数据转储到历史表
            markupRecordHisMapper.batInsert(auctionGoodsTmp.getId());
            markupRecordMapper.batDelete(auctionGoodsTmp.getId());
        }
    }

    /**
     * 拍卖校验 异步执行，开启新的事务处理
     *
     * @param auctionGoods
     * @return
     */

    @Transactional(rollbackFor = RuntimeException.class, timeout = 10)
    public void auctionCheck(AuctionGoods auctionGoods) {
        //统计该商品的参拍记录
        MarkupRecordSummary markupRecordSummary = markupRecordMapper.selectSummaryByGoodsId(auctionGoods.getId());

        //计算是否达到拍中规则
        //拍卖成功低价
        BigDecimal auctionLowPrice = auctionGoods.getGoodsPrice().add(auctionGoods.getProfit());
        //当前拍卖的所有资金
        BigDecimal auctionNowPrice = markupRecordSummary.getSumCommission().add(
                markupRecordSummary.getMaxCurrentBid()).subtract(markupRecordSummary.getSumCoupons());
        if (auctionNowPrice.compareTo(auctionLowPrice) >= 0) {
            log.info("商品[{}:{}]参拍成功处理", auctionGoods.getId(), auctionGoods.getGoodsName());
            boolean lockStatus=redisService.setnx(RedisConstant.RDS_AUCTION_LOCK_KEY_PREFIX+auctionGoods.getId(),auctionGoods.getId(),5L);
            if(lockStatus) {
                auctionSucess(auctionGoods, markupRecordSummary);
            }else{
                throw AuctionException.AUCTION_GOODS_NOT_EXIT.newInstance("手慢了，宝贝被其他人抢走啦~").print();
            }
        } else {
            //到期延期处理
            if (auctionGoods.getPostponeTime() > 0) {
                Date nowDate = new Date();
                //如果本次拍卖时间加顺延时间超过结束时间，则结束时间顺延
                if (auctionGoods.getEndTime().compareTo(DateTimeUtil.datePostpone(nowDate, auctionGoods.getPostponeTime() * ProjectConstant.TIME_UNIT)) <= 0) {
                    log.info("商品[{}:{}]延期处理", auctionGoods.getId(), auctionGoods.getGoodsName());
                    auctionGoods.setEndTime(DateTimeUtil.datePostpone(auctionGoods.getEndTime(), auctionGoods.getPostponeTime() * ProjectConstant.TIME_UNIT));
                    log.info("延期结束时间为:{}", auctionGoods.getEndTime());
                    auctionGoods.setUpdateTime(null);
                    auctionGoodsMapper.updateById(auctionGoods);
                }
            }
        }
    }

    @Transactional(rollbackFor = RuntimeException.class, timeout = 5)
    public  void auctionSucess(AuctionGoods auctionGoods, MarkupRecordSummary markupRecordSummary) {

        //查找拍中记录
        List<MarkupRecord> markupRecordList = markupRecordMapper.selectByGoodsIdAndCurrentBid(auctionGoods.getId(), markupRecordSummary.getMaxCurrentBid());
        if (markupRecordList.size() != 1) {
            //如果同时又两个人中奖不结束
            return;
        }

        //如果拍中则停止拍卖
        //已拍出
        auctionGoods.setStatus(2);
        auctionGoods.setUpdateTime(null);
        auctionGoodsMapper.updateById(auctionGoods);

        //生成拍中订单到订单表
        //拍中的加价记录
        MarkupRecord markupRecord = markupRecordList.get(0);
        //拍中用户的加价汇总记录
        MarkupRecordSummary markupRecordSummaryUserId = markupRecordMapper.selectSummaryByGoodsIdAndUserId(auctionGoods.getId(), markupRecord.getUserId());
        //实例化订单对象并赋值
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setOrderNumber(ProjectConstant.GOODS_ORDER + DateTimeUtil.getNowInSS() + markupRecord.getUserId());
        goodsOrder.setCustomerId(markupRecord.getUserId());
        goodsOrder.setGoodsId(auctionGoods.getId());
        goodsOrder.setBond(auctionGoods.getBond());
        goodsOrder.setTotalNumber(markupRecordSummaryUserId.getMaxShootingTimes());
        goodsOrder.setTotalNumberAll(markupRecordSummary.getMaxTotalNumber());
        goodsOrder.setReferenceCommission(markupRecordSummaryUserId.getSumCommission());
        goodsOrder.setReferenceCommissionAll(markupRecordSummary.getSumCommission());
        goodsOrder.setPreferentialAmount(markupRecordSummaryUserId.getSumCoupons());
        goodsOrder.setActualPayment(markupRecord.getCurrentBid());
        //尾款金额为实际出价减去保证金
        goodsOrder.setTailAmount(markupRecord.getCurrentBid().subtract(auctionGoods.getBond()));
        //订单状态为0-待支付
        goodsOrder.setStatus(0);
        log.info("订单信息:{}", goodsOrder.toString());
        goodsOrderMapper.insert(goodsOrder);
        //退还未拍中客户保证金到余额
        returnBond(auctionGoods, markupRecord.getUserId());
        //对代理进行分润
        shareProfit(auctionGoods);
        //数据转储到历史表
        markupRecordHisMapper.batInsert(markupRecord.getGoodsId());
        markupRecordMapper.batDelete(markupRecord.getGoodsId());

        //发送消息给拍中者
        Message message = new Message();
        //标志位未发送
        message.setSendFlag(0);
        //类型为用户消息
        message.setType(2);
        //读标志位未读
        message.setReadFlag(0);
        //默认生效
        message.setStatus(1);
        //暂时缓存拍卖成功标志1-拍中 2-没有拍中
        message.setRemark("1");
        message.setSubjectId(markupRecord.getUserId());
        message.setGoodsId(markupRecord.getGoodsId());
        message.setMessageInfo("恭喜您，竞拍成功");
        messageMapper.insert(message);

        //发送系统消息
        //类型为系统消息
        message.setId(null);
        message.setRemark(null);
        message.setType(1);
        message.setSubjectId(0);
        message.setGoodsId(goodsOrder.getGoodsId());
        Customer customer = customerMapper.selectById(goodsOrder.getCustomerId());
        message.setMessageInfo(customer.getName() + "以" + goodsOrder.getActualPayment() + "元 拍到了" + auctionGoods.getGoodsName());
        messageMapper.insert(message);
    }

    /**
     * 返还保证金到余额
     *
     * @param auctionGoods
     * @param auctionUser
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class, timeout = 5)
    public void returnBond(AuctionGoods auctionGoods, Integer auctionUser) {
        //查找保证金记录
        List<MarkupRecord> markupRecordList = markupRecordMapper.selectByGoodsIdAndBond(auctionGoods.getId());
        if (markupRecordList.size() <= 1) {
            //只有一人拍中
            log.info("只有一个人拍中了该商品:{}[{}]", auctionGoods.getId(), auctionGoods.getGoodsName());
            return;
        }
        //余额校验请求查询参数对象
        Account account = new Account();
        account.setType(ProjectConstant.ACCOUNT_TYPE_CUSTOMER);

        TransLog transLog = null;
        Message message = null;
        Account accountResult = null;

        for (MarkupRecord markupRecordTmp : markupRecordList) {
            if (markupRecordTmp.getUserId().equals(auctionUser)) {
                //拍中的客户不退换保证金
                continue;
            }
            account.setSubjectId(markupRecordTmp.getUserId());
            accountResult = accountMapper.selectBySubjectIdAndTpye(account);
            //退还保证金 账户如果为空 继续下次循环
            if (accountResult == null) {
                log.info("#退还保证金查询的账户对象为空值，查询参数{}", account.toString());
                continue;
            }

            //流水记录
            transLog = new TransLog();
            transLog.setOrderNumber(ProjectConstant.BOND_RETURN + DateTimeUtil.getNowInSS() + markupRecordTmp.getUserId());
            transLog.setFieldId(markupRecordTmp.getFieldId());
            transLog.setGoodsId(markupRecordTmp.getGoodsId());
            transLog.setSubject(markupRecordTmp.getUserId());
            transLog.setAccountType(accountResult.getType());
            transLog.setAccountId(accountResult.getId());
            transLog.setChannel(ProjectConstant.PAY_CHANNEL_BALANCE);
            transLog.setTransSign(1);
            transLog.setTransType(4);
            transLog.setAmount(markupRecordTmp.getBond());
            transLog.setPayAmount(markupRecordTmp.getBond());
            transLog.setStatus(1);
            transLogMapper.insert(transLog);

            //余额消费 balance 加上实际的支付金额
            accountResult.setBalance(accountResult.getBalance().add(markupRecordTmp.getBond()));
            accountMapper.updateBalanceById(accountResult);

            //发送消息给客户
            message = new Message();
            //标志位未发送
            message.setSendFlag(0);
            //类型为用户消息
            message.setType(2);
            //读标志位未读
            message.setReadFlag(0);
            //默认生效
            message.setStatus(1);
            //暂时缓存拍卖成功标志1-拍中 2-没有拍中
            message.setRemark("2");
            message.setSubjectId(markupRecordTmp.getUserId());
            message.setGoodsId(markupRecordTmp.getGoodsId());
            message.setMessageInfo("很遗憾，您没有拍到此商品");
            messageMapper.insert(message);
        }
    }

    /**
     * 佣金分润处理
     *
     * @param auctionGoods
     */
    @Transactional(rollbackFor = RuntimeException.class, timeout = 5)
    public void shareProfit(AuctionGoods auctionGoods) {
        BigDecimal bigValue100 = new BigDecimal(100);
        BigDecimal profitValue = new BigDecimal(100);

        //获取用户代理分润列表
        List<ShareProfit> shareProfitList = markupRecordMapper.selectShareProfit(auctionGoods.getId());
        if (shareProfitList.size() == 0) {
            log.info("该商品[{}:{}]没有可分润的代理", auctionGoods.getId(), auctionGoods.getGoodsName());
            return;
        }
        Account account = new Account();
        account.setType(ProjectConstant.ACCOUNT_YYPE_AGENT);

        TransLog transLog = null;
        Account accountResult = null;
        //遍历分润列表
        for (ShareProfit shareProfitTmp : shareProfitList) {
            //获取代理分润信息
            if (shareProfitTmp.getProportionType() == 1) {
                //如果分润类型为默认比例，则需要在分润模板表中查找默认比例
                ProfitModul profitModul = profitModulMapper.selectByGoodsPrice(auctionGoods.getGoodsPrice());
                //将模板比例赋值给个固定比例
                shareProfitTmp.setProportion(profitModul.getProportion());
                shareProfitTmp.setParentProportion(profitModul.getParentProportion());
            }
            //应分润金额
            profitValue = shareProfitTmp.getSumCommission().multiply(shareProfitTmp.getProportion().divide(bigValue100));

            //查询所属代理账号信息
            account.setSubjectId(shareProfitTmp.getId());
            accountResult = accountMapper.selectBySubjectIdAndTpye(account);
            if (accountResult == null) {
                log.info("#分润 查询的账户对象为空值，查询参数{}", account.toString());
                continue;
            }
            //组装交易信息
            transLog = new TransLog();
            transLog.setOrderNumber(ProjectConstant.SHARE_PROFIT + DateTimeUtil.getNowInSS() + shareProfitTmp.getId());
            transLog.setGoodsId(auctionGoods.getId());
            transLog.setSubject(shareProfitTmp.getId());
            transLog.setAccountType(accountResult.getType());
            transLog.setAccountId(accountResult.getId());
            transLog.setTransType(8);
            transLog.setTransSign(1);
            transLog.setStatus(1);

            //一级代理分润
            if (shareProfitTmp.getParentId() == null) {
                //直接分发应分润金额给本代理
                accountResult.setBalance(accountResult.getBalance().add(profitValue));
                transLog.setAmount(profitValue);
                transLog.setPayAmount(profitValue);
                accountMapper.updateBalanceById(accountResult);
                transLogMapper.insert(transLog);
                //二级代理分润
            } else if (shareProfitTmp.getParentId() > 0) {
                //父及代理分润
                BigDecimal profitValue1 = profitValue.multiply(shareProfitTmp.getProportion().divide(bigValue100));
                //二级代理（本代理）分润
                BigDecimal profitValue2 = profitValue.subtract(profitValue1);
                //分发二级代理（本代理）利润
                accountResult.setBalance(accountResult.getBalance().add(profitValue2));
                transLog.setAmount(profitValue2);
                transLog.setPayAmount(profitValue2);
                accountMapper.updateBalanceById(accountResult);
                transLogMapper.insert(transLog);

                //父及代理分润
                //获取父及代理账户
                account.setSubjectId(shareProfitTmp.getParentId());
                accountResult = accountMapper.selectBySubjectIdAndTpye(account);
                if (accountResult == null) {
                    log.info("#分润 查询的账户对象为空值，查询参数{}", account.toString());
                    continue;
                }
                //分发父级代理利润
                accountResult.setBalance(accountResult.getBalance().add(profitValue1));

                transLog.setId(null);
                transLog.setOrderNumber(ProjectConstant.SHARE_PROFIT + DateTimeUtil.getNowInSS() + shareProfitTmp.getParentId());
                transLog.setSubject(shareProfitTmp.getParentId());
                transLog.setAmount(profitValue1);
                transLog.setPayAmount(profitValue1);
                accountMapper.updateBalanceById(accountResult);
                transLogMapper.insert(transLog);
            }
        }
    }
}
