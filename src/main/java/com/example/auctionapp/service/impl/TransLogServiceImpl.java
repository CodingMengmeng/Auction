package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.core.ResultGenerator;
import com.example.auctionapp.dao.AccountMapper;
import com.example.auctionapp.dao.BankMapper;
import com.example.auctionapp.dao.TransLogMapper;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.entity.Bank;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.ITransLogService;
import com.example.auctionapp.util.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static com.example.auctionapp.core.Result.ResultCode.SUCCESS;

/**
 * <p>
 * 交易 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class TransLogServiceImpl  implements ITransLogService {

    @Resource
    TransLogMapper transLogMapper;

    @Resource
    AccountMapper accountMapper;
    @Resource
    private BankMapper bankMapper;

    @Override
    public Page<TransLog> selectByType(Page<TransLog> page, Integer customerId,int type) {
        List<TransLog> auctionGoods = transLogMapper.selectByType(page, customerId, type);
        page.setRecords(auctionGoods);
        return page;
    }

    /**
     * 客户充值
     * @param transLog
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean customerRecharge(TransLog transLog){
        //获取账户信息
        Account account = accountMapper.selectById(transLog.getAccountId());
        //更新账户充值
        account.setBalance(account.getBalance().add(transLog.getPayAmount()));
        accountMapper.updateBalanceById(account);
        //跟新交易信息为成功
        transLogMapper.updateSucessById(transLog);
        return true;
    }

    /**
     * 提现申请
     * @author马会春
     * @param transLog
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result applyForWithdraw(TransLog transLog) {

        /**
         * 查询账户余额是否大于提现金额
         */
        Account account = accountMapper.selectOne(new QueryWrapper<>(new Account()).eq("subject_id", transLog.getSubject()).eq("type",1));
        if (account.getBalance().compareTo(transLog.getAmount())==-1){
            return ResultGenerator.genSuccessMEssageResult("余额不足");
        }
        /**
         * 查询是否绑定银行卡
         */
        Bank bank = bankMapper.selectOne(new QueryWrapper<>(new Bank()).eq("id",transLog.getBankId()).eq("bank_card_no",transLog.getBankCardNo()));
        if (bank == null){
            return ResultGenerator.genSuccessMEssageResult("请绑定银行卡");
        }
        /**
         * 更新账户表，冻结提现金额
         */
        Account a = new Account();
        a.setId(account.getId());
        a.setFrozen(transLog.getPayAmount().add(account.getFrozen()));
        a.setBalance(account.getBalance().subtract(transLog.getPayAmount()));
        accountMapper.updateBalanceAndFrozenById(a);
        //账户类型是客户提现
        transLog.setTransType(2);
        //提现手续费 每一笔一元钱
        transLog.setFee(BigDecimal.valueOf(0.00));
        //提现状态为 未审核
        transLog.setStatus(0);
        //账户类型是客户类型
        transLog.setAccountType(1);
        transLog.setOrderNumber(ProjectConstant.WITHDRAW + DateTimeUtil.getNowInSS() + transLog.getSubject());
        int i = transLogMapper.insert(transLog);
        if (i>0){
            return ResultGenerator.genSuccessResult("申请成功，正在审核，请耐心等候");
        }
        return ResultGenerator.genSuccessMEssageResult("提现失败，系统错误");
    }

    @Override
    public Result getTransNotify(TransLog transLog) {

        TransLog transNotify = transLogMapper.getTransNotify(transLog);
        if (transNotify == null) {
            return Result.errorInfo(SUCCESS,"未找到交易信息!");
        }
        return Result.success(transNotify);
    }

}
