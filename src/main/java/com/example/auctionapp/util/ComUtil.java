package com.example.auctionapp.util;

import com.example.auctionapp.core.ProjectConstant;

/**
 * Created by Haidy on  2018-11-26
 * 文件上传工具类服务方法
 **/
public class ComUtil {
    public static String getOrderNumBer(Integer type, Object obj){
        String orderNumber ;
        switch(type){
            case 1 :
                orderNumber = ProjectConstant.CUSTOMER_RECHARGE + DateTimeUtil.getNowInSS() + obj;
                break;
            case 6 :
            case 7 :
                orderNumber = ProjectConstant.GOODS_AUCTION + DateTimeUtil.getNowInSS() + obj;
                break;
            default :
                orderNumber = ProjectConstant.GOODS_ORDER + DateTimeUtil.getNowInSS() + obj;
        }
        return orderNumber;
    }
}
