package com.example.auctionapp.wxapi.vo;

import java.io.Serializable;


/**
 * O
 * 客服消息  - 图文消息
 */

public class CustomerMsgResponseNews implements Serializable {

    private static final long serialVersionUID = -411579325670460757L;


    private String touser;
    private String msgtype;
    private CustomerMsgResponseVo news;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public CustomerMsgResponseVo getNews() {
        return news;
    }

    public void setNews(CustomerMsgResponseVo news) {
        this.news = news;
    }


}
