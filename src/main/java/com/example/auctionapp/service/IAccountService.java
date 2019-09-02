package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.Account;

import java.util.Map;

/**
 * <p>
 * 账户 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface IAccountService {


    /**
     * 修改支付密码
     *
     * @param account
     * @return
     */
    boolean updatePayPassword(Account account);

    /**
     * 获取账户信息
     *
     * @param account
     * @return
     */
    Map<String, Object> selectAccount(Account account);

    /**
     * 验证支付密码输入是否正确
     * @param account
     * @param password
     * @return
     */
    boolean verifyPayPass(Account account, String password);

    /**
     * 修改账户余额
     * @param account
     * @return
     */
    Integer updateBalanceById(Account account);
}
