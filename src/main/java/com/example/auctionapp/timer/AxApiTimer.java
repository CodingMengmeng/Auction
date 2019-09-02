package com.example.auctionapp.timer;

import com.example.auctionapp.wxapi.process.MpAccount;
import com.example.auctionapp.wxapi.process.WxApiClient;
import com.example.auctionapp.wxapi.process.WxMemoryCacheClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author zhuqi
 * 定时器
 */
@Slf4j
@Component
@EnableScheduling
public class AxApiTimer {


    @Autowired
    private WxApiClient wxApiClient;

    @Autowired
    private WxMemoryCacheClient wxMemoryCacheClient;

    /**
     * 微信公众号accessToken 信息5分钟刷新一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void syncAccessToken() {

        log.info("###定时任务获取微信的accessToken 当前时间戳：[{}]", System.currentTimeMillis());
        MpAccount mpAccount = wxMemoryCacheClient.getSingleMpAccount();
        log.info("##当前唯mpAccount =[{}]", mpAccount);

        // 刷新公众号的accesstoken
        wxApiClient.doRefreshAccessToken(mpAccount);
    }
}
