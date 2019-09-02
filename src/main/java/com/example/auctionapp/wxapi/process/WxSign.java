package com.example.auctionapp.wxapi.process;


import com.example.auctionapp.wxapi.util.SecurityUtil;
import com.example.auctionapp.wxapi.util.WxSignUtil;

import java.util.SortedMap;
import java.util.TreeMap;

public class WxSign {


    private String appId;
    private String timestamp;
    private String nonceStr;
    private String signature;

    public WxSign() {

    }

    public WxSign(String appId, String jsTicket, String url) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = SecurityUtil.getRandomString(8);
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("jsapi_ticket", jsTicket);
        map.put("noncestr", nonceStr);
        map.put("timestamp", timestamp);
        map.put("url", url);
        this.appId = appId;
        this.nonceStr = nonceStr;
        this.timestamp = timestamp;
        this.signature = WxSignUtil.signature(map);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WxSign{");
        sb.append("appId='").append(appId).append('\'');
        sb.append(", timestamp='").append(timestamp).append('\'');
        sb.append(", nonceStr='").append(nonceStr).append('\'');
        sb.append(", signature='").append(signature).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
