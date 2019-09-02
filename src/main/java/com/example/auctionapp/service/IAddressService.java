package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.Address;

import java.util.List;

/**
 * <p>
 * 地址 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-14
 */
public interface IAddressService {


    /**
     * 增加
     *
     * @param address
     * @return
     */
    Result insert(Address address);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean delete(int id);

    /**
     * 修改
     *
     * @param address
     * @return
     */
    Result update(Address address);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    Address select(int id);

    /**
     * 根据客户id查询出所有的地址
     *
     * @param customerId
     * @param type
     * @return
     */
    List<Address> selectList(int customerId, int type);

}
