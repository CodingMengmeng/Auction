package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.CouponManage;
import com.example.auctionapp.entity.TransLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单,红包,优惠券关联表 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface CouponManageMapper extends BaseMapper<CouponManage> {


    /**
     * 查询出客户优惠券
     *
     * @param page
     * @param customerId
     * @param status
     * @return
     */
    List<Map> selectDiscountCoupon(Page page, @Param("customerId") int customerId,
                                   @Param("status") int status);

    /**
     * 更新过期时间
     *
     * @param customerId
     * @return
     */
    int updateByExpireTime(@Param("customerId") int customerId);

    /**
     * 更新优惠券状态
     *
     * @param id
     * @return
     */
    int updateStatusByIdForUsed(Integer id);


    /**
     * 查询用户的活动优惠券数量
     *
     * @param customerId
     * @param couponsType 优惠券类型
     * @param sendType
     * @return
     */
    Integer getCouponManageByUserAndType(@Param("customerId") Integer customerId, @Param("couponsType") Integer couponsType, @Param("sendType") Integer sendType);

    /**
     * 获取用户的 某个活动类型的所有的优惠券
     *
     * @param customerId
     * @param couponsType
     * @return
     */
    List<CouponManage> getActivityCouponByUser(@Param("customerId") Integer customerId, @Param("couponsType") Integer couponsType);

    List<CouponManage> getCouponManageForTop(Integer couponsType);
}
