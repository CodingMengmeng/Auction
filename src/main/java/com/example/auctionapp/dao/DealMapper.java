package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.AuctionGoods;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

//出价&&成交mapper
public interface DealMapper extends BaseMapper<AuctionGoods> {

   Map<String,Object> selectAuctionMinParam(@Param("auctionGoodsId") String auctionGoodsId);

   int selectActualPeopleNum(@Param("auctionGoodsId") String auctionGoodsId);

}
