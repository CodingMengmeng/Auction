package com.example.auctionapp.wxapi.domain;

/**
 * 文本消息
 */
public class MsgText extends MsgBase {

    private static final long serialVersionUID = -6321632894378449160L;

    private String content;// 消息内容

    private Long baseId;// 消息主表id

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

}