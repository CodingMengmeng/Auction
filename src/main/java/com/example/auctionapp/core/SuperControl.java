package com.example.auctionapp.core;

public class SuperControl {
    private Integer number;
    private String  obj;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public SuperControl(Integer number, String obj) {
        this.number = number;
        this.obj = obj;
    }

    public SuperControl() {

    }


    public static SuperControl  getObject(Integer number, String obj){
        return new SuperControl(number, obj);
    }
}
