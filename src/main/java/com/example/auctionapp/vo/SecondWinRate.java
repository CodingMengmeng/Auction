package com.example.auctionapp.vo;

public class SecondWinRate {
    //第二次划分区间的拍中几率

    public SecondWinRate() {
    }
    public SecondWinRate(double lowRate, double highRate) {
        this.lowRate = lowRate;
        this.highRate = highRate;
    }

    double lowRate;
    double highRate;

    public double getLowRate() {
        return lowRate;
    }

    public void setLowRate(double lowRate) {
        this.lowRate = lowRate;
    }

    public double getHighRate() {
        return highRate;
    }

    public void setHighRate(double highRate) {
        this.highRate = highRate;
    }
}
