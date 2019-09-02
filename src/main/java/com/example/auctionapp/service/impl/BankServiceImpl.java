package com.example.auctionapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.dao.BankMapper;
import com.example.auctionapp.entity.Bank;
import com.example.auctionapp.service.IBankService;
import com.example.auctionapp.util.BankUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 银行卡 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class BankServiceImpl implements IBankService {


    @Resource
    BankMapper bankMapper;


    @Override
    public Result insert(Bank bank) {

        List<Bank> banks = bankMapper.selectList(new QueryWrapper<Bank>()
                .eq("bank_card_no", bank.getBankCardNo())
                .eq("user_id", bank.getUserId())
                .eq("acct_type", bank.getAcctType())
        );

        if (banks.size() > 0) {
            for (Bank b : banks) {
                if (b.getStatus() == 0) {
                    b.setStatus(1);
                    update(b);
                    return Result.success();
                }
            }
            return Result.errorInfo("银行卡号已存在!");
        }

        String s = BankUtils.bankAuthenticate(bank.getBankCardNo(), bank.getIdCard(), bank.getAcctName(), bank.getBankPhone());
        JSONObject jsonObject = JSON.parseObject(s);
        String respCode = jsonObject.getString("respCode");

        if ("0000".equals(respCode)) {

            bank.setBankName(jsonObject.getString("bankName"));
            return Result.success(bankMapper.insert(bank));
        }
        return Result.errorInfo(jsonObject.getString("respMessage"));
    }

    @Override
    public Result delete(int id) {
        return Result.success(bankMapper.deleteById(id));

    }

    @Override
    public Result update(Bank bank) {
        return Result.success(bankMapper.updateById(bank));

    }

    @Override
    public Bank selectById(int id) {
        return bankMapper.selectById(id);

    }

    @Override
    public List<Bank> selectList(int userId, int acctType) {
        return bankMapper.selectList(new QueryWrapper<Bank>()
                .eq("user_id", userId)
                .eq("acct_type", acctType)
                .eq("status", 1));
    }
}
