package com.example.auctionapp.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * <p>
 * 拍品 服务类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface IAliPayService {

    /**
     *支付宝 APP统一支付接口
     * @param orderNumber
     * @param totalAmount
     * @return
     */
    AlipayTradeAppPayResponse alipayTradeAppPay(String orderNumber, String totalAmount) throws AlipayApiException;

    /**
     * 支付宝 统一退款接口
     * @param outTradeNo
     * @param tradeNo
     * @param refundAmount
     * @param outRequestNo
     * @return
     * @throws AlipayApiException
     */
    AlipayTradeRefundResponse alipayTradeRefund(String outTradeNo, String tradeNo, String refundAmount, String outRequestNo) throws AlipayApiException;

    /**
     * 支付宝单笔转账接口
     * @param outTradeNo
     * @param transferAmount
     * @param payeeAccount
     * @param payeeName
     * @return
     */
    AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransfer(String outTradeNo, String transferAmount, String payeeAccount, String payeeName) throws AlipayApiException;

}
