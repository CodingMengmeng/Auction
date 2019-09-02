package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.dao.CouponManageMapper;
import com.example.auctionapp.entity.CouponManage;
import com.example.auctionapp.service.ICouponManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
@Slf4j
@Service
public class CouponManageServiceImpl implements ICouponManageService {

    @Resource
    CouponManageMapper couponManageMapper;

    @Override
    public Page<Map> selectByStatus(Page<Map> page, Integer customerId, int status) {

        couponManageMapper.updateByExpireTime(customerId);

        List<Map> auctionGoods = couponManageMapper.selectDiscountCoupon(page, customerId, status);
        page.setRecords(auctionGoods);
        return page;
    }
    /**
     *  查询用户的活动优惠券数量
     * @param customerId
     * @param couponsType 优惠券类型
     * @param sendType 发放类型
     * @return
     */
    @Override
    public Integer getCouponManageByUserAndType(Integer customerId, Integer couponsType,Integer sendType){
        return couponManageMapper.getCouponManageByUserAndType(customerId,couponsType,sendType);
    }

    /**
     * 获取用户的 某个活动类型的所有的优惠券
     * @param customerId
     * @param couponsType
     * @return
     */
    @Override
    public List<CouponManage> getActivityCouponByUser(Integer customerId, Integer couponsType){
        return couponManageMapper.getActivityCouponByUser(customerId,couponsType);
    }

    /**
     * 使用优惠券
     * @param id
     */
    @Override
    @Transactional(rollbackFor=Exception.class,timeout = 5)
    public boolean useCoupons(Integer id){
        CouponManage couponManage = couponManageMapper.selectById(id);
        if(couponManage == null){
            log.info("优惠信息[" + id + "]不存在");
            return false;
        }
        if(couponManage.getStatus() != 0)
        {
            log.info("优惠信息[" + id + "]已使用或已过期");
            return false;
        }
        couponManageMapper.updateStatusByIdForUsed(id);
        return true;
    }

    /**
     * 用户最新优惠券的数据
     * @param couponsType
     * @return
     */
    @Override
    public List<CouponManage> getCouponManageForTop(Integer couponsType){
        return couponManageMapper.getCouponManageForTop(couponsType);
    }

}
