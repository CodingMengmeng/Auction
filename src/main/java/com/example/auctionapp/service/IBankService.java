package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.Bank;

import java.util.List;

/**
 * <p>
 * 银行卡 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IBankService {


    /**
     * 增加
     *
     * @param bank
     * @return
     */
    Result insert(Bank bank);


    /**
     * 删除
     *
     * @param id
     * @return
     */
    Result delete(int id);

    /**
     * 修改
     *
     * @param bank
     * @return
     */
    Result update(Bank bank);

    /**
     * 根据id查询出一个
     *
     * @param id
     * @return
     */
    Bank selectById(int id);

    /**
     * 根据客户id查询出多个
     *
     * @param userId
     * @param acctType
     * @return
     */
    List<Bank> selectList(int userId, int acctType);
}
