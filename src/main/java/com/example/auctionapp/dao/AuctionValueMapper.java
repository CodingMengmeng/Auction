package com.example.auctionapp.dao;

import com.example.auctionapp.entity.AuctionValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;


/**
 * @description 拍卖值表接口
 * @author mengjia
 * @date 2019/9/4
 * @version 1.0
 * @see 
 */
public interface AuctionValueMapper extends BaseMapper<AuctionValue>{


    /**
     * @description 更新拍卖值表的数据
     * @author mengjia
     * @date 2019/9/4
     * @param auctionValue 更新的数据
     * @return java.lang.Integer
     * @throws
     **/
    Integer updateAuctionValueData(AuctionValue auctionValue);
    /**
     * 根据客户编号和拍品编号查询拍卖值表数据
     * @param customerId 客户编号
     * @Param goodsId 拍品编号
     * @return
     */
    AuctionValue selectAuctionValueInfoById(@Param("customerId") Integer customerId, @Param("goodsId") Integer goodsId);
}
