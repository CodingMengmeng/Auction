package com.example.auctionapp.dao;

import com.example.auctionapp.entity.Coupons;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 优惠券 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface CouponsMapper extends BaseMapper<Coupons> {
    /**
     * 根据sendType查询可发放优惠券列表
     * @param sendType
     * @return
     */
    List<Coupons> selectListBySendType(Integer sendType);
}
