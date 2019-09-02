package com.example.auctionapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.GoodsOrder;
import com.example.auctionapp.entity.TransLog;
import org.apache.ibatis.annotations.Param;
import com.example.auctionapp.core.Result;

import java.util.Map;

/**
 * <p>
 * 拍品订单 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IGoodsOrderService {

    /**
     * 查询出客户订单的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<Map> selectCustomerOrderInfo(Page page, @Param("customerId") Integer customerId,
                                      @Param("status") Integer status);

    /**
     * 订单余额支付支付
     *
     * @param transLog
     */
    Result orderPayByBalance(TransLog transLog);

    /**
     * 订单第三方支付
     *
     * @param transLog
     */
    boolean orderPayByThird(TransLog transLog);


    /**
     * 修改
     *
     * @param goodsOrder
     * @return
     */
    boolean update(GoodsOrder goodsOrder);


    /**
     * 根据商品id查询出商品
     *
     * @param goodsId 商品id
     * @return
     */
    GoodsOrder getGoodsOrderByGoodsId(Integer goodsId);
}
