package com.example.auctionapp.controller.pay;

import com.alibaba.fastjson.JSONObject;
import com.example.auctionapp.annotation.WebLog;
import com.example.auctionapp.core.ProjectConstant;
import com.example.auctionapp.core.ComEnum;
import com.example.auctionapp.dao.AccountMapper;
import com.example.auctionapp.dao.TransLogMapper;
import com.example.auctionapp.entity.Account;
import com.example.auctionapp.entity.TransLog;
import com.example.auctionapp.service.IPayService;
import com.example.auctionapp.service.IWxPayService;
import com.example.auctionapp.service.impl.WxPayServiceImpl;
import com.example.auctionapp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.*;

@RequestMapping("/pay/wxpay")
@RestController
@Slf4j
public class WxpayController {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private TransLogMapper transLogMapper;

    @Autowired
    private IPayService iPayService;

    @Autowired
    private IWxPayService iWxPayService;


    @PostMapping("/wxpay")
    @WebLog("微信支付请求")
    public JSONObject wxpay(HttpServletRequest request, @RequestBody TransLog transLog) {
        //交易的类型,实际支付金额,交易金额,交易发起者
        if (transLog.getTransType() == null ||
                transLog.getPayAmount() == null ||
                transLog.getAmount() == null ||
                transLog.getSubject() == null ||
                StringUtils.isEmpty(transLog.getChannel())) {
            log.info("参数不全，空值返回");
            return JSONObject.parseObject("参数不全，空值返回");
        }


        if (StringUtils.isEmpty(transLog.getChildChannel())){
            transLog.setChildChannel("1");
        }

        if (!transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_APP.getType()) && !transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_H5.getType()) && !transLog.getChildChannel().equals(ComEnum.childChannel.CHILD_CHANNEL_TYPE_MP.getType())){
            return JSONObject.parseObject("参数错误，请确认支付方式");
        }

        if (transLog.getTransType() == 10 && (transLog.getOrderNumber() == null || transLog.getAddressId() == null)) {
            log.info("参数不全，空值返回");
            return JSONObject.parseObject("参数不全，空值返回");
        }

        //构造支付请求报文
        String orderNumBer = ComUtil.getOrderNumBer(transLog.getTransType(), transLog.getSubject());
        Map<String, Object> billMessage = iWxPayService.createBillMessage(orderNumBer,request, transLog);

        if (billMessage == null || billMessage.get("package").equals("FAIL")) {
            return JSONObject.parseObject("创单失败");
        }
        //获取订单号
        //获取账户信息
        Account account = new Account();
        account.setSubjectId(transLog.getSubject());
        account.setType(ProjectConstant.ACCOUNT_TYPE_CUSTOMER);
        //根据主体id和账户类型查询出账户信息
        account = accountMapper.selectBySubjectIdAndTpye(account);
        //记录交易流水表
        transLog.setAccountId(account.getId());
        transLog.setRemark(transLog.getOrderNumber());
        transLog.setOrderNumber(orderNumBer);
        transLog.setAccountType(account.getType());
        //第三方支付状态为待支付
        transLog.setTransStatus(0);
        //待支付
        transLog.setStatus(3);
        transLog.setChildChannel(ComEnum.childChannel.getChannel(transLog.getChildChannel()));
        log.info("table trans_log data:{}", transLog);
        transLogMapper.insert(transLog);
        JSONObject ret = new JSONObject(billMessage);
        log.info("把封装好的XML返回给前端,param-->{}", ret);
        return ret;
    }

    @PostMapping("/notifyHandler")
    public String  notifyHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader reader = request.getReader();
        log.info("reader --> {}", reader);

        //读取行写入String对象
        StringBuffer inputString = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        request.getReader().close();

        //转化为map对象
        log.info("----接收到的报文---- {}" + inputString.toString());
        Map<String, String> map = new HashMap();
        if (inputString.toString().length() > 0) {
            map = XMLUtils.parseXmlToList(inputString.toString());
        } else {
            log.error("接受微信报文为空");
            return ProjectConstant.NOTIFY_MSG_FAIL;
        }

        //验签
        boolean signFlag = WxPayServiceImpl.examineSign(map, map.get("sign"));
        if (signFlag == false) {
            log.error("支付异步回调验签失败");
            return ProjectConstant.NOTIFY_MSG_FAIL;
        }
        log.info("微信报文转化为map集合-->{}", map);

        //支付成功后的回调处理
        //判断，首先更新交易表中的状态  然后对相应的操作做数据表处理  如果失败则进行退款操作
        //判断 操作类型 如果是拍卖支付，判断 如果在付钱后拍卖结束后了，这时需要进行退款操作；拍卖没有结束 则操作加价表 交易表等
        //如果是充值 则操作账户表  交易表等
        TransLog transLog = transLogMapper.selectByOrderNumber(map.get("out_trade_no"));
        if (transLog == null || transLog.getTransStatus() != 0) {
            log.info("订单：" + map.get("out_trade_no") + "重复回调已忽略");
            return ProjectConstant.NOTIFY_MSG_SUCCESS;
        }
        if (map != null && "SUCCESS".equals(map.get("result_code"))) {
            //transLogMapper.updateTransStatusByOrderNumber();
            //根据唯一订单号查询订单信息
            transLog.setTransStatus(1);
            transLog.setTransCode(map.get("transaction_id"));
            //更新交易表中的状态
            transLogMapper.updateTransStatusByOrderNumber(transLog);

            //执行退款操作
            if (!iPayService.PayNotify(map.get("transaction_id"), map.get("out_trade_no"))) {
                transLog = transLogMapper.selectByOrderNumber(map.get("out_trade_no"));

                //开始退款 先修改退款状态
                transLog.setTransStatus(5);
                transLog.setTransCode(map.get("transaction_id"));
                transLogMapper.updateTransStatusByOrderNumber(transLog);

                //微信退款接口
                String reqs = iWxPayService.wxpayTradeRefund(map, transLog);
                log.info("接收退款返回参数-->{}", reqs);

                Map xmlToList = XMLUtils.parseXmlToList(reqs);
                JSONObject payData = new JSONObject(xmlToList);

                if ("SUCCESS".equals(payData.getString("result_code"))) {
                    transLog.setTransStatus(4);//退款成功
                    transLog.setTransCode(map.get("transaction_id"));
                    transLogMapper.updateTransStatusByOrderNumber(transLog);
                } else {
                    transLog.setTransStatus(3);//待退款
                    transLog.setTransCode(map.get("transaction_id"));
                    transLogMapper.updateTransStatusByOrderNumber(transLog);
                }
            }
            return ProjectConstant.NOTIFY_MSG_SUCCESS;

        } else if ("FAIL".equals(map.get("result_code"))) {
            //支付失败
            //交易关闭，更新状态为失败  操作交易表
            transLog.setTransStatus(2);//支付失败
            transLog.setTransCode(map.get("transaction_id"));
            transLogMapper.updateTransStatusByOrderNumber(transLog);
        } else {
            log.error("异常,param-->{}",map);
        }
        return ProjectConstant.NOTIFY_MSG_FAIL;
    }

}
