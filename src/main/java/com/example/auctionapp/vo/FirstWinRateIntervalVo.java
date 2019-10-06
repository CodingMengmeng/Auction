package com.example.auctionapp.vo;

import java.math.BigDecimal;
import java.util.List;

//拍中几率 第一次划分区间 vo
public class FirstWinRateIntervalVo {
    BigDecimal intervalLowValue;//拍卖值的左区间
    BigDecimal intervalHighValue;//拍卖值的右区间
    BigDecimal winRateLowValue;//拍中几率的左区间
    BigDecimal winRateHighValue;//拍中几率的右区间
    int intervalPeopleNum;//落在区间内的人数
    List<SecondWinRateIntervalVo> secondWinRateIntervalVoList;// 1个第一次的区间对应1-多个二次划分区间


    public FirstWinRateIntervalVo(){}

    public FirstWinRateIntervalVo(BigDecimal intervalLowValue, BigDecimal intervalHighValue, BigDecimal winRateLowValue, BigDecimal winRateHighValue, int intervalPeopleNum) {
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

    public BigDecimal getWinRateLowValue() {
        return winRateLowValue;
    }

    public void setWinRateLowValue(BigDecimal winRateLowValue) {
        this.winRateLowValue = winRateLowValue;
    }

    public BigDecimal getWinRateHighValue() {
        return winRateHighValue;
    }

    public void setWinRateHighValue(BigDecimal winRateHighValue) {
        this.winRateHighValue = winRateHighValue;
    }

    public int getIntervalPeopleNum() {
        return intervalPeopleNum;
    }

    public void setIntervalPeopleNum(int intervalPeopleNum) {
        this.intervalPeopleNum = intervalPeopleNum;
    }

    public List<SecondWinRateIntervalVo> getSecondWinRateIntervalVoList() {
        return secondWinRateIntervalVoList;
    }

    public void setSecondWinRateIntervalVoList(List<SecondWinRateIntervalVo> secondWinRateIntervalVoList) {
        this.secondWinRateIntervalVoList = secondWinRateIntervalVoList;
    }
}
