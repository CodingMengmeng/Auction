package com.example.auctionapp.service;

import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.TransLog;

/**
 * <p>
 * 拍品 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IPayService {

    /**
     * 三方支付回调统一处理接口
     * @param transCode
     * @param orderNumber
     * @return
     */
    boolean PayNotify(String transCode, String orderNumber);

    /**
     * 三方支付回调失败处理
     * @param transCode
     * @param orderNumber
     * @return
     */
    void PayNotifyFailure(String transCode, String orderNumber);

}
