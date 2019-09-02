package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.service.ICustomerService;
import com.example.auctionapp.vo.BadgeLevelVo;
import com.example.auctionapp.vo.CustomerDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-06
 */
public interface CustomerMapper extends BaseMapper<Customer> {


    /**
     * 获取用户和用户账户信息
     *
     * @param id
     * @return
     */
    Map selectCustomerAndAccount(@Param("id") int id);

    CustomerDataVo selectCustomerDataAndAccount(@Param("id")Integer id);

    List<BadgeLevelVo> selectBadgeLevelById(@Param("id")Integer id);

    /**
     * 查询出客户的 {参拍总和,关注总和,历史浏览总和,优惠券总和}
     *
     * @param id 客户id
     * @return {参拍总和,关注总和,历史浏览总和,优惠券总和}
     */
    Map selectCustomerGoodsSketch(@Param("id") int id);

    /**
     * 根据用户id查询出订单的状态条数
     *
     * @param id 客户id
     * @return 订单的状态条数
     */
    List<Map<String, Integer>> selectOrderInfo(@Param("id") int id);

    /**
     * 查询出猜你喜欢
     *
     * @param page
     * @param id
     * @return
     */
    List<AuctionGoods> selectGuessYouLike(Page page, @Param("id") int id);

    /**
     * 验证
     * @param customerId
     * @return
     */
    Map selectVerifyAuthen(@Param("customerId") Integer customerId);
}
