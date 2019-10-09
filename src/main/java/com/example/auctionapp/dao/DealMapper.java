package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.AuctionGoodsDealInfo;
import com.example.auctionapp.entity.AuctionValue;
import com.example.auctionapp.vo.BadgeCustomerVo;
import com.example.auctionapp.vo.DealConditionVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//出价&&成交mapper
public interface DealMapper extends BaseMapper<AuctionGoods> {

   Map<String,Object> selectAuctionMinParam(@Param("auctionGoodsId") int auctionGoodsId);

   int selectActualPeopleNum(@Param("auctionGoodsId") int auctionGoodsId);

   int selectRankFirstUserId(@Param("auctionGoodsId") int auctionGoodsId);

   BigDecimal selectMaxBid(@Param("auctionGoodsId") int auctionGoodsId,@Param("customer_id") int customer_id);


   BigDecimal selectTotalPayedBeans(@Param("auctionGoodsId") int auctionGoodsId,@Param("customer_id") int customer_id);
   //查询默认返佣比例
   BigDecimal selectAgentProportion(@Param("customer_id") int customer_id);
   //查询固定返佣比例
   BigDecimal selectProfitModulProportion(@Param("commission") BigDecimal commission);
   //更新总排豆值
   Integer updateBeansPonds(@Param("beansPond") BigDecimal beansPond,@Param("auctionGoodsId") int auctionGoodsId);
   //查询总拍豆
   BigDecimal selectBeansPond(@Param("auctionGoodsId") int auctionGoodsId);
   //更新用户返佣
   Integer updateCustomerCommision(@Param("beansPond") BigDecimal beansPond,@Param("customerCommision") BigDecimal customerCommision,@Param("auctionGoodsId") int auctionGoodsId);

   AuctionGoodsDealInfo selectDealInfoById(@Param("auctionGoodsId") int auctionGoodsId);

   BigDecimal getWinRateByCustomerId(@Param("customerId") int customerId,@Param("goodsId")int goodsId);


}
