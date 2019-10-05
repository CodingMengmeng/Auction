package com.example.auctionapp.vo;

import java.math.BigDecimal;

public class SecondWinRateIntervalVo {
    //第二次拍中几率划分区间vo

    public SecondWinRateIntervalVo() {
    }
    public SecondWinRateIntervalVo(BigDecimal lowRate, BigDecimal highRate) {
        this.lowRate = lowRate;
        this.highRate = highRate;
    }

    BigDecimal lowRate;
    BigDecimal highRate;

    public BigDecimal getLowRate() {
        return lowRate;
    }

    public void setLowRate(BigDecimal lowRate) {
        this.lowRate = lowRate;
    }

    public BigDecimal getHighRate() {
        return highRate;
    }

    public void setHighRate(BigDecimal highRate) {
        this.highRate = highRate;
    }
}
