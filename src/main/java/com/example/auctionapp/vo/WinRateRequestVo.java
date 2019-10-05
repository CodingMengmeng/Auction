package com.example.auctionapp.vo;

import java.math.BigDecimal;

//拍中几率查询接口请求对象
public class WinRateRequestVo {
    BigDecimal goodsValue;//拍卖值
    int customerId;//客户id

    public BigDecimal getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(BigDecimal goodsValue) {
        this.goodsValue = goodsValue;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
