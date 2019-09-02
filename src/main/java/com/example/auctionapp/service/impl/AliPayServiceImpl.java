package com.example.auctionapp.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



/**
 * <p>
 * 拍品 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Slf4j
@Service
public class AliPayServiceImpl implements IAliPayService {

    @Value("${third.return.host}")
    private String host;

    /**
     *阿里APP统一支付接口
     * @param orderNumber
     * @param totalAmount
     * @return
     */
    @Override
    public AlipayTradeAppPayResponse alipayTradeAppPay(String orderNumber, String totalAmount) throws AlipayApiException{
        //实例化客户端
        //dev支付宝沙箱
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ProjectConstant.APP_ID, ProjectConstant.APP_PRIVATE_KEY, "json", ProjectConstant.CHARSET, ProjectConstant.ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("七拍");
        model.setSubject("七拍");
        model.setOutTradeNo(orderNumber);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totalAmount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        //禁用信用卡渠道
        model.setDisablePayChannels("creditCard,credit_group");
        request.setBizModel(model);
        request.setReturnUrl(host+"/pay/alipay/gateway");
        request.setNotifyUrl(host+"/pay/alipay/notify");

        try {
            return alipayClient.sdkExecute(request);
        }catch (AlipayApiException e){
            throw e;
        }
    }

    /**
     * 支付宝 统一退款接口
     * @param outTradeNo
     * @param tradeNo
     * @param refundAmount
     * @param outRequestNo
     * @return
     * @throws AlipayApiException
     */
    @Override
    public AlipayTradeRefundResponse alipayTradeRefund(String outTradeNo, String tradeNo, String refundAmount, String outRequestNo) throws AlipayApiException {
        log.info("########支付退款 outTradeNo={}，transactionNo={},refundAmt={},refoundNumber={} ####",outTradeNo,tradeNo,refundAmount,outRequestNo);

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ProjectConstant.APP_ID, ProjectConstant.APP_PRIVATE_KEY, "json", ProjectConstant.CHARSET, ProjectConstant.ALIPAY_PUBLIC_KEY, "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        model.setRefundAmount(refundAmount);
        model.setRefundReason("正常退款");
        model.setOutRequestNo(outRequestNo);
        request.setBizModel(model);
        try {
            return alipayClient.execute(request);
        }catch (AlipayApiException e){
            throw e;
        }
    }
    /**
     * 支付宝单笔转账接口
     * @param outTradeNo
     * @param transferAmount
     * @param payeeAccount
     * @param payeeName
     * @return
     */
    @Override
    public AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransfer(String outTradeNo, String transferAmount, String payeeAccount, String payeeName) throws AlipayApiException{
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ProjectConstant.APP_ID, ProjectConstant.APP_PRIVATE_KEY, "json", ProjectConstant.CHARSET, ProjectConstant.ALIPAY_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outTradeNo);
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount(payeeAccount);
        model.setAmount(transferAmount);
        model.setPayerShowName("七拍");
        model.setPayeeRealName(payeeName);
        request.setBizModel(model);
        try {
            return alipayClient.execute(request);
        }catch (AlipayApiException e){
            throw e;
        }
    }
}
