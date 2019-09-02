package com.example.auctionapp;

import com.example.auctionapp.wxapi.process.MpAccount;
import com.example.auctionapp.wxapi.process.WxMemoryCacheClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhuqi
 * @Date 2019-06-27
 * @Version 1.0
 **/

@Configuration
public class CommandLineRunnerInit implements CommandLineRunner {


    @Value("${wechat.mp.account}")
    private String account;

    @Value("${wechat.mp.appid}")
    private String appid;

    @Value("${wechat.mp.appsecret}")
    private String appsecret;

    @Value("${wechat.mp.token}")
    private String token;

    @Override
    public void run(String... strings) throws Exception {
        MpAccount mpAccount=new MpAccount();
        mpAccount.setAccount(account);
        mpAccount.setAppid(appid);
        mpAccount.setAppsecret(appsecret);
        mpAccount.setToken(token);
        mpAccount.setMsgcount(5);
        WxMemoryCacheClient.addMpAccount(mpAccount);
    }

}
