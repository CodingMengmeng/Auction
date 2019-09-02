package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.dao.AccountMapper;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.service.IAccountService;
import com.example.auctionapp.util.Md5SaltTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 账户 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    AccountMapper accountMapper;

    @Override
    public boolean updatePayPassword(Account account) {
        return accountMapper.update(account, new UpdateWrapper<Account>()
                .eq("subject_id", account.getSubjectId())
                .eq("type", account.getType())) > 0;
    }

    /**
     * 获取账户信息
     *
     * @param account
     * @return
     */
    @Override
    public Map<String, Object> selectAccount(Account account){
        //账户类型设置成客户账户
        account.setType(1);
        account = accountMapper.selectBySubjectIdAndTpye(account);
        //使用map处理过滤敏感信息
        Map<String, Object> accountMap = new HashMap<String, Object>();
        accountMap.put("id",account.getId());
        accountMap.put("type",account.getType());
        accountMap.put("subjectId",account.getSubjectId());
        accountMap.put("balance",account.getBalance());
        accountMap.put("frozen", account.getFrozen());
        if(account.getPassword() == null || "".equals(account.getPassword())) {
            //未设置密码
            accountMap.put("passwordFlag", 0);
        }else {
            //已设置密码
            accountMap.put("passwordFlag", 1);
        }
        return accountMap;
    }


    @Override
    public boolean verifyPayPass(Account account, String password) {
        String encryptedPwd = Md5SaltTool.getCustomerPwdMd5(password);
        return encryptedPwd.equals(account.getPassword());
    }

    /**
     * 修改账户余额
     * @param account
     * @return
     */
    @Override
    @Transactional(rollbackFor=RuntimeException.class,timeout = 5)
    public Integer updateBalanceById(Account account){
        return accountMapper.updateBalanceById(account);

    }
}
