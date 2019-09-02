package com.example.auctionapp.controller;


import com.example.auctionapp.core.Result;
import com.example.auctionapp.wxapi.process.*;
import com.example.auctionapp.wxapi.service.WxApiService;
import com.example.auctionapp.wxapi.util.WxSignUtil;
import com.example.auctionapp.wxapi.vo.MsgRequest;
import com.example.auctionapp.wxapi.vo.SemaphoreMap;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 微信与开发者服务器交互接口
 */
@Slf4j
@RestController
@RequestMapping("/wxapi")
public class WxApiCtrl {

    @Autowired
    private WxMemoryCacheClient wxMemoryCacheClient;

    @Autowired
    private WxApiService wxApiService;

    @Autowired
    private WxApiClient wxApiClient;

    /**
     * GET请求：进行URL、Tocken 认证； 1. 将token、timestamp、nonce三个参数进行字典序排序 2.
     * 将三个参数字符串拼接成一个字符串进行sha1加密 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    @RequestMapping(value = "/{account}/message", method = RequestMethod.GET)
    public  void doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String account) {
        // 如果是多账号，根据url中的account参数获取对应的MpAccount处理即可
        MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();// 获取缓存中的唯一账号
        log.info("当前公众号信息：{}", mpAccount.toString());
        if (mpAccount != null) {
            String token = mpAccount.getToken();// 获取token，进行验证；
            String signature = request.getParameter("signature");// 微信加密签名
            String timestamp = request.getParameter("timestamp");// 时间戳
            String nonce = request.getParameter("nonce");// 随机数
            String echostr = request.getParameter("echostr");// 随机字符串

            // 校验成功返回 echostr，成功成为开发者；否则返回error，接入失败
            if (WxSignUtil.validSign(signature, token, timestamp, nonce)) {
                try {
                    response.getOutputStream().println(echostr);
                }catch (IOException ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    /**
     * POST 请求：进行消息处理(核心业务controller)
     */
    @RequestMapping(value = "/{account}/message", method = RequestMethod.POST)
    @ResponseBody
    public String doPost(HttpServletRequest request, HttpServletResponse response, @PathVariable String account) {
        try {
            MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();

            String token = mpAccount.getToken();// token
            String appId = mpAccount.getAppid();
            String signature = request.getParameter("msg_signature");// 微信加密签名
            String timestamp = request.getParameter("timestamp");// 时间戳
            String nonce = request.getParameter("nonce");// 随机数
            MsgRequest msgRequest = MsgXmlUtil.parseXml(request, appId, token, signature, timestamp, nonce);// 获取发送的消息

            log.info("当前公众号msgRequest消息：{}", msgRequest.toString());

            String msgId = msgRequest.getMsgId();
            String createTimeAndFromUserName = msgRequest.getCreateTime() + msgRequest.getFromUserName();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(msgId)) {
                if (SemaphoreMap.getSemaphore().containsKey(msgId)) {
                    log.info("消息重复推送msgId [{}]--->FromUserName [{}], MsgType [{}], Event [{}]", msgId,
                            msgRequest.getFromUserName(), msgRequest.getMsgType(), msgRequest.getEvent());
                    return "success";
                } else {
                    SemaphoreMap.getSemaphore().put(msgId, msgId);
                }
            } else if (StringUtils.isNotEmpty(createTimeAndFromUserName)) {
                if (SemaphoreMap.getSemaphore().containsKey(createTimeAndFromUserName)) {
                    log.info("消息重复推送createTimeAndFromUserName [{}]--->FromUserName [{}], MsgType [{}], Event [{}]",
                            createTimeAndFromUserName, msgRequest.getFromUserName(), msgRequest.getMsgType(), msgRequest.getEvent());
                    return "success";
                } else {
                    SemaphoreMap.getSemaphore().put(createTimeAndFromUserName, createTimeAndFromUserName);
                }
            } else {
                log.info("消息重复推送msgId [{}]--->FromUserName [{}], MsgType [{}], Event [{}]", msgId,
                        msgRequest.getFromUserName(), msgRequest.getMsgType(), msgRequest.getEvent());
                return "success";
            }

            // 处理完业务逻辑后回复微信平台
            String rtnXml=wxApiService.processMsg(msgRequest, mpAccount);

            if (StringUtils.isBlank(rtnXml) || "null".equals(rtnXml)) {
                return "success";
            } else if ("success".equals(rtnXml) || "error".equals(rtnXml)) {
                return rtnXml;
            } else {
                return rtnXml;
            }
        } catch (Exception e) {
            log.error("## 公众号消息处理发生异常：{}", e);
            return "error";
        }
    }


    /**
     * 获取js ticket
     *
     * @param request
     * @param url
     * @return
     */
    @RequestMapping(value = "/jsTicket")
    @ResponseBody
    public Result jsTicket(HttpServletRequest request, String url) {
        // 获取缓存中的唯一账号
        MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();
        String jsTicket = wxApiClient.getJSTicket(mpAccount);
        WxSign sign = new WxSign(mpAccount.getAppid(), jsTicket, url);
        log.info("jsTicket 返回页面的值是[{}]",sign);
        return Result.success(sign);
    }
}
