package com.example.auctionapp.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.auctionapp.core.Result;
import com.example.auctionapp.entity.TransLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 拍品 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IWxPayService {

    /**
     *微信 APP统一支付接口
     * @param orderNumber
     * @param totalAmount
     * @return
     */
    AlipayTradeAppPayResponse wxpayTradeAppPay(String orderNumber, String totalAmount) throws AlipayApiException;


    /**
     * 支付宝 统一退款接口
     * @param outTradeNo
     * @param tradeNo
     * @param refundAmount
     * @param outRequestNo
     * @return
     * @throws AlipayApiException
     */
    AlipayTradeRefundResponse wxpayTradeRefund(String outTradeNo, String tradeNo, String refundAmount, String outRequestNo) throws AlipayApiException;

    /**
     * 构建微信支付统一下单请求报文
     * @param request
     * @param transLog
     * @return
     */
    Map<String,Object> createBillMessage(String orderNumBer,HttpServletRequest request, TransLog transLog);


    /**
     * 微信退款申请接口
     */
    String wxpayTradeRefund(Map<String,String> maps, TransLog transLog);

    /**
     * 公众号登录时，获取用户授权信息
     * @param code
     * @return
     */
    Result getUserinfo(String code);
}
