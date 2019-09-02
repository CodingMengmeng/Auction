package com.example.auctionapp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class XiaojingkuUtil {

    public static String sign(Map<String, String> data, String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String src = convertMap2String(data);
        src = src + SdkConstants.AMPERSAND + "key" + SdkConstants.EQUAL + secretKey;
        return md5(src).toUpperCase();
    }

    private static String md5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] result = md.digest(s.getBytes("utf-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            int num = b & 0xff;
            String str = Integer.toHexString(num);
            if (str.length() == 1) {
                sb.append("0");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public static String convertMap2String(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<>();
        data.forEach((key, value) -> {
            //签名参数与空值参数不参与签名
            if (!SdkConstants.req_param_sign.equals(key.trim()) &&
                    !SdkConstants.res_param_sign.equals(key.trim()) &&
                    !isEmpty(value)) {
                tree.put(key, value);
            }
        });
        StringBuffer sb = new StringBuffer();
        tree.forEach((key, value) -> {
            sb.append(key).append(SdkConstants.EQUAL).append(value).append(SdkConstants.AMPERSAND);
        });
        return sb.substring(0, sb.length() - 1);
    }

    public static Map<String, String> convertString2Map(String result) {
        Map<String, String> map = null;

        if (!isEmpty(result)) {
            if (result.startsWith("{") && result.endsWith("}")) {
                result = result.substring(1, result.length() - 1);
            }
            map = parseResultString(result);
        }

        return map;
    }

    private static Map<String, String> parseResultString(String str) {
        Map<String, String> map = new HashMap<>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if(len>0){
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key
                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else  {// 如果当前生成的是value
                    if(isOpen){
                        if(curChar == openName){
                            isOpen = false;
                        }
                    }else{//如果没开启嵌套
                        if(curChar == '{'){//如果碰到，就开启嵌套
                            isOpen = true;
                            openName ='}';
                        }
                        if(curChar == '['){
                            isOpen = true;
                            openName =']';
                        }
                    }

                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }
            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("ResultString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("ResultString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }
}
