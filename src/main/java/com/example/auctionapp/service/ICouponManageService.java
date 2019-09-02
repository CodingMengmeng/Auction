package com.example.auctionapp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.entity.CouponManage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单,红包,优惠券关联表 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface ICouponManageService {


    /**
     * 查询出客户优惠券
     *
     * @param page
     * @param customerId
     * @param status
     * @return
     */
    Page<Map> selectByStatus(Page<Map> page, Integer customerId, int status);

    /**
     * 使用优惠券
     *
     * @param id
     * @return
     */
    boolean useCoupons(Integer id);

    /**
     * 查询用户的活动优惠券数量
     *
     * @param customerId
     * @param couponsType 优惠券类型
     * @param sendType    发放类型
     * @return
     */
    Integer getCouponManageByUserAndType(Integer customerId, Integer couponsType, Integer sendType);


    /**
     * 获取用户的 某个活动类型的所有的优惠券
     *
     * @param customerId
     * @param couponsType
     * @return
     */
    List<CouponManage> getActivityCouponByUser(Integer customerId, Integer couponsType);

    /**
     * 用户最新优惠券的数据
     *
     * @param couponsType
     * @return
     */
    List<CouponManage> getCouponManageForTop(Integer couponsType);
}
