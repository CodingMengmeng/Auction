package com.example.auctionapp.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.ComEnum;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.dao.AccountMapper;
import com.example.auctionapp.dao.TransLogMapper;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.IAliPayService;
import com.example.auctionapp.service.IPayService;
import com.example.auctionapp.util.ComUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ProjectName: dgt-pay
 * @Package: com.bjst.dgt.controller
 * @ClassName: AlipayPayController
 * @Description: 支付宝
 * @Author: yidong
 * @CreateDate: 2018/07/11 10:27 AM
 */
@Api(value = "支付宝支付接口")
@Slf4j
@Controller
@RequestMapping("/pay/alipay")
public class AlipayController {

    @Value("${third.return.host}")
    private String host;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransLogMapper transLogMapper;

    @Autowired
    private IPayService iPayService;

    @Autowired
    private IAliPayService iAliPayService;

    @ApiOperation(value = "支付宝充值接口", notes = "支付宝充值接口", produces = "application/json")
    @PostMapping("getpay")
    @WebLog(value = "支付宝支付请求")
    public void alipay(HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse, @RequestBody TransLog transLog) throws ServletException, IOException {
        //参数校验
        //交易的类型,实际支付金额,交易金额,交易发起者
        if (transLog.getTransType() == null ||
                transLog.getPayAmount() == null ||
                transLog.getAmount() == null ||
                transLog.getSubject() == null ||
                transLog.getChannel() == null ) {
            log.error("参数不全，空值返回");
            return;
        }

        if (StringUtils.isEmpty(transLog.getChildChannel())){
            transLog.setChildChannel("1");
        }

        if (!transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_APP.getType()) && !transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_H5.getType()) && !transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_MP.getType())){
            log.error("参数错误，请确认支付方式");
            return;
        }

        //类型为订单尾款支付，订单号必填
        if (transLog.getTransType() == 10 && (transLog.getOrderNumber() == null || transLog.getAddressId() == null)) {
            log.error("参数不全，空值返回");
            return;
        }
        //获取订单号
        String orderNumber = ComUtil.getOrderNumBer(transLog.getTransType(), transLog.getSubject());
        //获取账户信息
        Account account = new Account();
        account.setSubjectId(transLog.getSubject());
        account.setType(ProjectConstant.ACCOUNT_TYPE_CUSTOMER);
        //根据主体id和账户类型查询出账户信息
        account = accountMapper.selectBySubjectIdAndTpye(account);

        //记录交易流水表
        transLog.setRemark(transLog.getOrderNumber());
        transLog.setOrderNumber(orderNumber);
        transLog.setAccountType(account.getType());
        transLog.setAccountId(account.getId());
        //第三方支付状态为待支付
        transLog.setTransStatus(0);
        //待支付
        transLog.setChildChannel(ComEnum.childChannel.getChannel(transLog.getChildChannel()));
        transLog.setStatus(3);
        log.info("table trans_log data:{}", transLog);
        transLogMapper.insert(transLog);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = iAliPayService.alipayTradeAppPay(orderNumber, transLog.getPayAmount().toString());
            //就是orderString 可以直接给客户端请求，无需再做处理。
            log.info("支付宝支付同步返回：{}", response.getBody());
            httpResponse.getWriter().write(response.getBody());
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (AlipayApiException e) {
            log.error("#支付宝支付同步返回 发生异常 {}", e);
        }
    }

    /**
     * 支付宝网关  用户支付成功后 支付宝会把这个地址告诉用户 然后用户会访问这个地址 后台敷衍一下 提示成功就好了
     *
     * @return
     */
    @GetMapping("gateway")
    @ApiIgnore
    public String returnUrl() {
        return "pay/pay-ok";
    }

    /**
     * 授权回调地址 用户支付成功后 支付宝会直接访问这个地址 然后后台处理具体的业务逻辑
     * 通知触发条件
     * 触发条件名	触发条件描述	触发条件默认值
     * TRADE_FINISHED	交易完成	false（不触发通知）
     * TRADE_SUCCESS	支付成功	true（触发通知）
     * WAIT_BUYER_PAY	交易创建	false（不触发通知）
     * TRADE_CLOSED	    交易关闭	true（触发通知）
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @WebLog(value = "支付宝异步回调")
    @ApiOperation(value = "支付宝异步回调接口", notes = "支付宝异步回调接口", produces = "application/json")
    @PostMapping(value = "notify")
    public void notifyUrl(HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        log.info("支付宝异步返回：{}", JSON.toJSONString(requestParams));
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。验签
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean flag = AlipaySignature.rsaCheckV1(params, ProjectConstant.ALIPAY_PUBLIC_KEY, ProjectConstant.CHARSET, "RSA2");
        if (flag) {
            //null   交易流水号    唯一订单号
            String tradeStatus = params.get("trade_status");
            String transactionNo = params.get("trade_no");
            String outTradeNo = params.get("out_trade_no");
            //获取订单信息    根据唯一订单号查询订单信息
            TransLog transLog = transLogMapper.selectByOrderNumber(outTradeNo);
            //如果重复接受回调，忽略
            if (transLog.getTransStatus() != 0) {
                response.getWriter().println("success");
                log.info("success");
                log.info("订单：" + outTradeNo + "重复回调已忽略");
                return;
            }
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                response.getWriter().println("success");
                log.info("success");
                //支付成功，更新三方交易状态为，支付成功
                transLog.setTransStatus(1);
                transLog.setTransCode(transactionNo);
                //更新交易表中的状态
                transLogMapper.updateTransStatusByOrderNumber(transLog);
                //交易成功触发
                //根据唯一订单号查询出trans_log表中的信息   然后根据交易的类型处理相关操作1 为客户充值  7为余额拍卖   10为订单支付
                //客户充值 更新账户表  更新交易表中的状态    余额拍卖     订单支付更新订单信息  更新交易表状态
                if (!iPayService.PayNotify(transactionNo, outTradeNo)) {
                    //本地业务处理失败则退款
                    //获取订单号
                    String refoundNumber = ComUtil.getOrderNumBer(transLog.getTransType(), transLog.getSubject());

                    //开始退款 先修改退款状态
                    transLog.setTransStatus(5);
                    transLog.setTransCode(transactionNo);
                    transLogMapper.updateTransStatusByOrderNumber(transLog);
                    try {
                        //支付宝退款接口
                        AlipayTradeRefundResponse alipayTradeRefundResponse = iAliPayService.alipayTradeRefund(outTradeNo, transactionNo, transLog.getPayAmount().toString(), refoundNumber);
                        if (alipayTradeRefundResponse.isSuccess()) {
                            log.info("##支付退款成功 outTradeNo={},transactionNo={}.refoundNumber={}", outTradeNo, transactionNo, refoundNumber);
                            //退款成功，跟新交易状态为退款成功
                            transLog.setTransStatus(4);
                            transLog.setTransCode(transactionNo);
                            transLogMapper.updateTransStatusByOrderNumber(transLog);
                        } else {
                            log.info("##支付退款失败 outTradeNo={},transactionNo={}.refoundNumber={}", outTradeNo, transactionNo, refoundNumber);
                            //支付失败，跟新交易状态为待退款，有运营介入处理
                            transLog.setTransStatus(3);
                            transLog.setTransCode(transactionNo);
                            transLogMapper.updateTransStatusByOrderNumber(transLog);
                        }
                    } catch (AlipayApiException e) {
                        log.error("##支付退款发生异常 outTradeNo={},transactionNo={}.refoundNumber={},异常信息：{}", outTradeNo, transactionNo, refoundNumber, e);
                    }
                }
            } else if ("TRADE_CLOSED".equals(tradeStatus)) {
                response.getWriter().println("success");
                log.info("success");
                //交易关闭更新
                //支付失败，更新三方交易状态为，支付失败
                transLog.setTransStatus(2);
                transLog.setTransCode(transactionNo);
                transLogMapper.updateTransStatusByOrderNumber(transLog);
                iPayService.PayNotifyFailure(transactionNo, outTradeNo);
            }
        }
    }
}
