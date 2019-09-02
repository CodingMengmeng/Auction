package com.example.auctionapp.util;


import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class CodeUtils {
    /**
     * 随机获取6位数
     * @return
     */
    public static String getNumber4FromRandom(){
        Random r = new Random();
        int xx = r.nextInt(10000);
        while(xx<1000){
            xx = r.nextInt(10000);
        }
        return String.valueOf(xx);
    }

    /**
     *
     * @title getRandomNumbernStr
     * @description 生成由数字组成的随机字符串
     * @param length 生成的字符串长度
     * @return String
     */
    public static String getRandomNumbernStr(int length) {
        return getRandomStr(length, "0123456789");
    }

    /**
     *
     * @title getRandomStr
     * @description 生成随机字符串
     * @author 朱秋友
     * @param length 生成的字符串长度
     * @param source 组成字符串的字符
     * @return String
     */
    public static String getRandomStr(int length, String source) {
        if (length <= 0) {
            return "";
        }

        if (StringUtils.isEmpty(source)) {
            source = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }

        int range = source.length();
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            buffer.append(source.charAt(random.nextInt(range)));
        }

        return buffer.toString();
    }
}
