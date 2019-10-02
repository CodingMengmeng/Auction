package com.example.auctionapp.vo;

import java.math.BigDecimal;
import java.util.List;

public class WinRateVo {
    //拍中几率VO
    BigDecimal intervalLowValue;//拍卖值的左区间
    BigDecimal intervalHighValue;//拍卖值的右区间
    double winRateLowValue;//拍中几率的左区间
    double winRateHighValue;//拍中几率的右区间
    int intervalPeopleNum;//落在区间内的人数
    List<SecondWinRate> secondWinRateList;

    public WinRateVo(){}

    public WinRateVo(BigDecimal intervalLowValue, BigDecimal intervalHighValue, double winRateLowValue, double winRateHighValue, int intervalPeopleNum) {
        this.intervalLowValue = intervalLowValue;
        this.intervalHighValue = intervalHighValue;
        this.winRateLowValue = winRateLowValue;
        this.winRateHighValue = winRateHighValue;
        this.intervalPeopleNum = intervalPeopleNum;
    }

    public BigDecimal getIntervalLowValue() {
        return intervalLowValue;
    }

    public void setIntervalLowValue(BigDecimal intervalLowValue) {
        this.intervalLowValue = intervalLowValue;
    }

    public BigDecimal getIntervalHighValue() {
        return intervalHighValue;
    }

    public void setIntervalHighValue(BigDecimal intervalHighValue) {
        this.intervalHighValue = intervalHighValue;
    }

    public double getWinRateLowValue() {
        return winRateLowValue;
    }

    public void setWinRateLowValue(double winRateLowValue) {
        this.winRateLowValue = winRateLowValue;
    }

    public double getWinRateHighValue() {
        return winRateHighValue;
    }

    public void setWinRateHighValue(double winRateHighValue) {
        this.winRateHighValue = winRateHighValue;
    }

    public int getIntervalPeopleNum() {
        return intervalPeopleNum;
    }

    public void setIntervalPeopleNum(int intervalPeopleNum) {
        this.intervalPeopleNum = intervalPeopleNum;
    }

    public List<SecondWinRate> getSecondWinRateList() {
        return secondWinRateList;
    }

    public void setSecondWinRateList(List<SecondWinRate> secondWinRateList) {
        this.secondWinRateList = secondWinRateList;
    }
}
