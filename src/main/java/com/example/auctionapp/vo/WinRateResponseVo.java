package com.example.auctionapp.vo;

import java.math.BigDecimal;

//拍中几率查询接口返回对象
public class WinRateResponseVo {
    BigDecimal goodsValue;//拍卖值
    int customerId;//客户id
    BigDecimal winRate;//拍中几率

    public WinRateResponseVo(BigDecimal goodsValue, int customerId, BigDecimal winRate) {
        this.goodsValue = goodsValue;
        this.customerId = customerId;
        this.winRate = winRate;
    }

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

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }
}
