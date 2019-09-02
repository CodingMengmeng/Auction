package com.example.auctionapp.util;

import java.util.*;

public class StringUtils {
    /**
     * 元转换成分
     * @param amount
     * @return
     */
    public static String getMoney(String amount) {
        if(amount==null){
            return "";
        }
        // 金额转化为分为单位
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0L;
        if(index == -1){
            amLong = Long.valueOf(currency+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
        }
        return amLong.toString();
    }


    public static String getHeadPhoto(){
        String passWord="";
        Random random = new Random();
        //length为几位密码
        for(int j = 0; j < 6; j++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                passWord += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                passWord += String.valueOf(random.nextInt(10));
            }
        }
        return passWord;
    }

    /***
     * <p>随机生成手机号码
     * @author hqwui
     */
    public static String getTelephone() {
        String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String thrid=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+thrid;
    }

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    public static void main(String[] args) {

    }

    /**
     * 将map集合封装为报文
     * @param map
     * @return
     */
    public static String getKeyValue(Map<String,String> map){
        StringBuffer reqContent = new StringBuffer();
        Set<String> keySet = map.keySet();
        List<String> list = new ArrayList<>(keySet);
        Collections.sort(list);
        for (String key : list) {
            String value = map.get(key);
            if (org.apache.commons.lang3.StringUtils.isEmpty(value)){
                continue;
            }
            reqContent.append("&" + key + "=" + value);

        }
        String content = reqContent.toString();
        if (content.startsWith("&")){
            content = content.replaceFirst("&","");
        }
        return content;
    }

}
