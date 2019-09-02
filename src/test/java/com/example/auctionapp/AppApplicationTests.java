package com.example.auctionapp;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.core.RedisConstant;
import com.example.auctionapp.dao.AuctionGoodsMapper;
import com.example.auctionapp.dao.CustomerMapper;
import com.example.auctionapp.dao.GoodsOrderMapper;
import com.example.auctionapp.dao.TransLogMapper;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.*;
import com.example.auctionapp.util.ComUtil;
import com.example.auctionapp.util.XMLUtils;
import com.example.auctionapp.vo.AuctionGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableTransactionManagement
@EnableScheduling
@Slf4j
public class AppApplicationTests {

    @Autowired
    private IRedisService iRedisService;

    @Resource
    IAliPayService iAliPayService;

    @Resource
    TransLogMapper transLogMapper;

    @Resource
    AuctionGoodsMapper auctionGoodsMapper;

    @Resource
    IAuctionGoodsService auctionGoodsService;

    @Resource
    ITransLogService transLogService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    IWxPayService iWxPayService;

    @Resource
    IMarkupRecordService markupRecordService;


    @Resource
   private IRedisService redisService;

    @Test
    public void testQuote() throws InterruptedException {

        Page<AuctionGoodsVO> auctionGoodsPage = auctionGoodsService.selectCustomerInCompete(new Page(1, 10), 3);
        System.out.println(auctionGoodsPage);
    }

    @Test
    public void testTuiKuan() {
        TransLog transLog = new TransLog();

        Map<String, String> map = new HashMap<>();
        String outTradeNo = "CR201907081015329593";
        map.put("transaction_id", "4200000330201907088444542569");
        //transLog = transLogMapper.selectByOrderNumber(outTradeNo);
        log.info("----------------------{}",transLog);
        String reqs = iWxPayService.wxpayTradeRefund(map, transLog);
        log.info("接收请求参数-->{}", reqs);
        Map xmlToList = XMLUtils.parseXmlToList(reqs);
        JSONObject payData = new JSONObject(xmlToList);
        TransLog trans1Log = new TransLog();
        trans1Log.setOrderNumber(outTradeNo);
        if ("SUCCESS".equals(payData.getString("return_code"))) {
            trans1Log.setTransStatus(4);//退款成功
            trans1Log.setTransCode(map.get("trade_no"));
            transLogMapper.updateTransStatusByOrderNumber(trans1Log);
        } else {
            trans1Log.setTransStatus(3);//待退款
            trans1Log.setTransCode(map.get("trade_no"));
            transLogMapper.updateTransStatusByOrderNumber(trans1Log);
        }
    }


    @Test
    public void testAlipayTuiKuan() throws Exception {
        TransLog transLog = new TransLog();
        String outTradeNo = "CR201907081016416043";
        String transactionNo = "2019062822001481750537189544";
        transLog = transLogMapper.selectByOrderNumber(outTradeNo);
        if (transLog == null) {
            log.error("selectByOrderNumber  is null");
        }
        transactionNo = transLog.getTransCode();
        String refoundNumber = ComUtil.getOrderNumBer(transLog.getTransType(), transLog.getSubject());
        AlipayTradeRefundResponse alipayTradeRefundResponse = iAliPayService.alipayTradeRefund(outTradeNo, transactionNo, transLog.getPayAmount().toString(), refoundNumber);
        log.info("接收返回参数，param-->{}", alipayTradeRefundResponse.toString());
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
    }

    @Test
    public void testAlipay() throws Exception {
        String orderNumber = ComUtil.getOrderNumBer(1, 123);
        try {
            AlipayTradeAppPayResponse alipayTradeAppPayResponse = iAliPayService.alipayTradeAppPay(orderNumber, "0.01");
            log.info("支付宝支付同步返回：{}", alipayTradeAppPayResponse.getBody());
        } catch (AlipayApiException e) {
            log.error("#支付宝支付同步返回 发生异常 {}", e);
        }
    }


    @Test
    public void testRedis() throws Exception {
        String googsId="12";
        boolean lockStatus=redisService.setnx(RedisConstant.RDS_AUCTION_LOCK_KEY_PREFIX+googsId,googsId,5L);
        System.out.println("第一次setnx lockStatus="+lockStatus);
         lockStatus=redisService.setnx(RedisConstant.RDS_AUCTION_LOCK_KEY_PREFIX+googsId,googsId,5L);
        System.out.println("第二次次setnx lockStatus="+lockStatus);
    }
}
