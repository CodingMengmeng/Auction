package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.AuctionGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.entity.GoodsOrder;
import com.example.auctionapp.entity.MarkupRecord;
import com.example.auctionapp.vo.AuctionGoodsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface AuctionGoodsMapper extends BaseMapper<AuctionGoods> {

    /**
     * 查询正在拍卖的拍品
     *
     * @param ipage
     * @param name
     * @return
     */
    List<Map<String, Object>> proceedAuctionGoods(IPage ipage, @Param("name") String name);

    List<Map<String, Object>> polling(IPage ipage);


    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param page
     * @return
     * @author 孔邹祥
     */
    List<AuctionGoods> selectProceedAuctionGoods(Page page);

    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<AuctionGoods> selectCustomerCompete(Page page, @Param("customerId") Integer customerId);


    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<AuctionGoodsVO> selectCustomerInCompete(Page page, @Param("customerId") Integer customerId);


    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<AuctionGoodsVO> selectCustomerOutCompete(Page page, @Param("customerId") Integer customerId);

    /**
     * 查询出用户正在关注的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<AuctionGoods> selectCustomerAttention(Page<AuctionGoods> page, @Param("customerId") Integer customerId);


    /**
     * 查询出用户浏览的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<AuctionGoods> selectCustomerBrowse(Page<AuctionGoods> page, @Param("customerId") Integer customerId);

    /**
     * 查询出客户订单的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    List<AuctionGoods> selectCustomerOrderInfo(Page<AuctionGoods> page, @Param("customerId") Integer customerId);

    /**
     * 根据id获取拍品信息
     *
     * @param auctionGoods
     * @return
     */
    Map<String, Object> getGoodsDetailById(AuctionGoods auctionGoods);

    /**
     * 详情页中我的出价
     *
     * @param auctionGoods
     * @return
     */
    List<Map<String, Object>> getMarkupRecord(AuctionGoods auctionGoods);

    /**
     * 根据类型查询出拍品
     *
     * @param page
     * @param type
     * @param sort
     * @return
     */
    List<AuctionGoods> selectByType(Page page, @Param("type") int type, @Param("sort") int sort);

    /**
     * 获取过期但未拍出的拍品列表
     *
     * @return
     */
    List<AuctionGoods> getExpireAuctionGoods();

    /**
     * 获取所有正在上拍的拍品Id
     *
     * @return
     */
    List<Integer> selectAuctionGoodsOfIdList();

    /**
     * @param id
     * @return
     */
    List<Map<String, Object>> selectRandomAuctionGoods(List<Integer> id);


}
