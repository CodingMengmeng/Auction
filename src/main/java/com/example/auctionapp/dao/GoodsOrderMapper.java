package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.GoodsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品订单 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {


    /**
     * 查询出客户订单的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<Map> selectCustomerOrderInfo(Page page, @Param("customerId") Integer customerId,
                                    @Param("status") Integer status);

    /**
     * 根据订单编号获取订单信息
     * @param orderNumber
     * @return
     */
    GoodsOrder selectByOrderNumber(String orderNumber);

}
