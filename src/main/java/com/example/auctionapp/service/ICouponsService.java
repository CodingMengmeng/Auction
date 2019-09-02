package com.example.auctionapp.service;


/**
 * <p>
 * 优惠券 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface ICouponsService  {

    /**
     * 优惠券类型{1:权利,2:折扣,3:现金,4:抽奖券}
     */
    public static final int couponsType1=1;
    public static final int couponsType2=2;
    public static final int couponsType3=3;
    public static final int couponsType4=4;


    /**
     * 发放优惠券
     * @param sendType 1:新用户注册,2:客户邀请好友,3:被客户邀请,4:拍卖
     * @param customerId
     */
    void sendCoupons(Integer sendType, Integer customerId);

}
