package com.example.auctionapp.entity.ext;

import com.example.auctionapp.entity.Customer;

public class CustomerExt extends Customer {

    /**
     * 验证码类型 1，注册  2，忘记密码
     */
    private Integer type;

    /**
     * 手机验证码
     */
    private String sms;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    @Override
    public String toString() {
        return "CustomerExt{" +
                "type=" + type +
                ", sms='" + sms + '\'' +
                '}';
    }
}
