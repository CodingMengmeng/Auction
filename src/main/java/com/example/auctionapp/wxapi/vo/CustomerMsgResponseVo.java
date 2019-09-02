package com.example.auctionapp.wxapi.vo;

import java.io.Serializable;
import java.util.List;


/**
 * O
 * 客服消息  - 图文消息
 */

public class CustomerMsgResponseVo implements Serializable {

    private static final long serialVersionUID = 5897445400568378773L;

    private List<ArticleInfo> Articles;

    public List<ArticleInfo> getArticles() {
        return Articles;
    }

    public void setArticles(List<ArticleInfo> articles) {
        Articles = articles;
    }


}
