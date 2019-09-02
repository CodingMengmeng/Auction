package com.example.auctionapp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by zll on 2018/7/12.
 */
public class NumberFormat {

    /**
     * 随机获取6位数
     * @return
     */
    public static String getNumber4FromRandom() {
        Random r = new Random();
        int xx = r.nextInt(1000000);
        while (xx < 100000) {
            xx = r.nextInt(1000000);
        }
        return String.valueOf(xx);
    }

    /**
     * 按四舍五入保留指定小数位数，位数不够用0补充
     * @param d：格式化前的小数
     * @param newScale：保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimalWithZero(double d, int newScale) {
        String pattern = "0.";
        for (int i = 0; i < newScale; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }


    /**
     * 按四舍五入保留指定小数位数，位数不够用0补充
     * @param d 格式化前的小数 String形式
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimalWithZero(String d, int newScale) {
        String pattern = "0.";
        for (int i = 0; i < newScale; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(Double.valueOf(d));
    }


    /**
     * 按四舍五入保留指定小数位数，小数点后仅保留有效位数
     * @param d 格式化前的小数
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimal(double d, int newScale) {
        String pattern = "#.";
        for (int i = 0; i < newScale; i++) {
            pattern += "#";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }


    /**
     * 按四舍五入保留指定小数位数，小数点后仅保留有效位数
     * @param d 格式化前的小数
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimal(String d, int newScale) {
        String pattern = "#.";
        for (int i = 0; i < newScale; i++) {
            pattern += "#";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(Double.valueOf(d));
    }

    /**
     * 将数字的字符串转化为货币格式的字符串
     * @param str
     * @return
     */
    public static String stringToCurrency(String str){
        int size = str.contains(".")?str.indexOf("."):str.length();
        int mod = size%3==0?3:size%3;
        int count = size%3==0?size/3-1:size/3;
        int i=0;
        String result=str.substring(0,mod);
        while(i<count){
            result += ","+str.substring(3*i+mod,3*i+mod+3);
            i++;
        }
        result += str.contains(".")?str.substring(str.indexOf(".")):"";
        return result;
    }


}
