package com.example.auctionapp.dao;

import com.example.auctionapp.entity.RankingList;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @description 
 * @author mengjia
 * @date 2019/9/30
 * @version 1.0
 * @see 
 */
public interface RankingListMapper extends BaseMapper<RankingList> {

    /**
     * @description 根据拍品ID和客户ID查询排行信息，返回一条记录
     * @author mengjia
     * @date 2019/9/30
     * @param goodsId 拍品ID
     * @param customerId 客户ID
     * @return com.example.auctionapp.entity.RankingList
     * @throws
     **/
    RankingList selectByGoodsIdAndCustomerId(@Param("goodsId")Integer goodsId,@Param("customerId")Integer customerId);

    /**
     * @description 根据拍品ID查询排行信息，返回该拍品所有客户的记录，按拍卖值从小到大排序
     * @author mengjia
     * @date 2019/9/30
     * @param goodsId 拍品ID
     * @return java.util.List<com.example.auctionapp.entity.RankingList>
     * @throws
     **/
    List<RankingList> selectByGoodsId(@Param("goodsId")Integer goodsId);

    /**
     * @description 根据拍品ID和客户ID更新排行表中的拍卖值
     * @author mengjia
     * @date 2019/9/30
     * @param rankingList
     * @return java.lang.Integer
     * @throws
     **/
    Integer updateGoodsValue(RankingList rankingList);

    /**
     * @description 根据拍品ID和客户ID更新排行表中的排名
     * @author mengjia
     * @date 2019/9/30
     * @param rankingList
     * @return java.lang.Integer
     * @throws
     **/
    Integer updateRank(RankingList rankingList);
    /**
     * @description 根据拍品ID和客户ID更新排行表中的拍中几率
     * @author mengjia
     * @date 2019/9/30
     * @param rankingList
     * @return java.lang.Integer
     * @throws
     **/
    Integer updateWinRate(RankingList rankingList);

    Integer deleteByGoodsIdAndCustomerId(@Param("goodsId") Integer goodsId,@Param("customerId") Integer customerId);


}
