package com.example.auctionapp.core;

import lombok.Data;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 *
 * @author zwl
 */
@Data
public class TokenModel {

    //用户id
    private long userId;
    //随机生成的uuid
    private String token;
    //是否实名认证
    private Integer cdCard;

    public TokenModel() {

    }

    public TokenModel(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public TokenModel(long userId, String token, Integer cdCard) {
        this.userId = userId;
        this.token = token;
        this.cdCard = cdCard;
    }




}
