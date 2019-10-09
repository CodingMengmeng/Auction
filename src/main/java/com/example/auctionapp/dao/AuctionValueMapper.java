package com.example.auctionapp.dao;

import com.example.auctionapp.entity.AuctionValue;
import com.example.auctionapp.vo.WinRateRequestVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;


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


    /**
     * @description 根据拍品编号和拍中用户编号清空拍卖值记录
     * @author mengjia
     * @date 2019/10/6
     * @param goodsId 拍品编号
     * @param customerId 拍中用户编号
     * @return java.lang.Integer
     * @throws
     **/
    Integer deleteByGoodsIdAndCustomerId(@Param("goodsId") Integer goodsId,@Param("customerId") Integer customerId);

    /**
     * @description 查询某拍品的所有用户和对应的拍卖值数据
     * @author mengjia
     * @date 2019/10/9
     * @param goodsId 拍品编号
     * @return com.example.auctionapp.vo.WinRateRequestVo
     * @throws
     **/
    List<WinRateRequestVo> selectAuctionValueInfoByGoodsId(@Param("goodsId") Integer goodsId);
}
