package com.example.auctionapp.service.impl;

import com.example.auctionapp.dao.CouponManageMapper;
import com.example.auctionapp.dao.CouponsMapper;
import com.example.auctionapp.entity.CouponManage;
import com.example.auctionapp.entity.Coupons;
import com.example.auctionapp.service.ICouponsService;
import com.example.auctionapp.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 优惠券 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
@Slf4j
public class CouponsServiceImpl  implements ICouponsService {

    @Resource
    CouponsMapper couponsMapper;

    @Resource
    CouponManageMapper couponManageMapper;

    /**
     * 发放优惠券
     * @param sendType 1:新用户注册,2:客户邀请好友,3:被客户邀请,4:拍卖
     * @param customerId
     */
    @Override
    @Transactional(rollbackFor=RuntimeException.class,timeout = 5)
    public void sendCoupons(Integer sendType, Integer customerId){
        //根据发放类型查询当前可发放优惠券
        List<Coupons> couponsList = couponsMapper.selectListBySendType(sendType);
        CouponManage couponManage = new CouponManage();
        for (Coupons couponsTmp : couponsList){
            //发放优惠券
            couponManage.setCouponId(couponsTmp.getId());
            //优惠
            couponManage.setType(1);
            couponManage.setSubjectId(customerId);
            //客户
            couponManage.setSubjectType(1);
            //状态 0 待使用
            couponManage.setStatus(0);
            //判断优惠券有效期类型
            if(couponsTmp.getExpireType() == 1){
                //有效期类型为截止日期
                couponManage.setExpireTime(couponsTmp.getExpireDate());
            }else if (couponsTmp.getExpireType() == 2){
                //有效期为一段周期
                couponManage.setExpireTime(DateTimeUtil.datePostpone(new Date(), couponsTmp.getExpireCycle() * 24 * 60 * 60));
            }else {
                log.info("有效期类型不正确");
                continue;
            }
            //将优惠券分发给客户
            couponManageMapper.insert(couponManage);
        }
    }
}
