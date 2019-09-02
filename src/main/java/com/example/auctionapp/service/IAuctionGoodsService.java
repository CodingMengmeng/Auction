package com.example.auctionapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.FollowRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.auctionapp.core.Paging;
import com.example.auctionapp.entity.Banner;
import com.example.auctionapp.vo.AuctionGoodsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IAuctionGoodsService {


    /**
     * 根据id获取拍品信息
     *
     * @param auctionGoods
     * @return
     */
    Map<String, Object> getGoodsDetailById(AuctionGoods auctionGoods);


    /**
     * 根据id获取拍品信息
     *
     * @param id
     * @return
     */
    AuctionGoods getAuctionGoodsById(int id);

    /**
     * 客户添加或取消关注
     *
     * @param followRecord
     * @return
     */
    Result addFollow(FollowRecord followRecord);

    /**
     * 详情页中已拍下客户信息
     *
     * @param goodsId
     * @return
     */
    Result getSuccessPat(Integer goodsId);

    /**
     * 查询正在拍卖的拍品
     *
     * @param paging
     * @return
     */
    IPage<Map<String, Object>> proceedAuctionGoods(Paging paging);

    /**
     * 首页轮询
     */
    IPage<Map<String, Object>> polling();

    /**
     * 首页轮播图
     *
     * @return
     */
    List<Banner> slideshow();

    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<AuctionGoods> selectCustomerCompete(Page<AuctionGoods> page, Integer customerId);

    /**
     * 查询出用户正在关注的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<AuctionGoods> selectCustomerAttention(Page<AuctionGoods> page, Integer customerId);

    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<AuctionGoodsVO> selectCustomerInCompete(Page page, @Param("customerId") Integer customerId);

    /**
     * 查询出用户正在参拍的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<AuctionGoodsVO> selectCustomerOutCompete(Page page, Integer customerId);

    /**
     * 查询出用户浏览的商品
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<AuctionGoods> selectCustomerBrowse(Page<AuctionGoods> page, Integer customerId);


    /**
     * 根据类型查询出所有的商品
     *
     * @param page
     * @return
     */
    IPage<AuctionGoods> selectByType(Page<AuctionGoods> page, int type, int sort);

    /**
     * 详情页中我的出价
     *
     * @param auctionGoods
     * @return
     */
    List<Map<String, Object>> getMarkupRecord(AuctionGoods auctionGoods);

    /**
     * 获取所有正在上拍的拍品Id
     *
     * @return
     */
    List<Integer> getAuctionGoodsOfIdList();

    List<Map<String, Object>> getRandomAuctionGoods(List<Integer> list);

}
