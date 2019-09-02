package com.example.auctionapp.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateTimeUtil {

    /**
     * 只是单纯根据A市场规律判断，没有添加特定的国假
     * @return
     */
    public static boolean isOpeningNow() {

        //设置时间格式
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String dateStr = sdf.format(new Date());
        Calendar cal = Calendar.getInstance();
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if ( dayWeek !=Calendar.MONDAY && dayWeek != Calendar.SATURDAY) {
            if ((dateStr.compareTo("09:30") >= 0 && dateStr.compareTo("11:30") <= 0) ||
                    (dateStr.compareTo("13:00") >= 0 && dateStr.compareTo("15:00") <= 0) ) {
                return true;
            }
        }
            return false;
    }



    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return "htlh"+newDate+result;
    }

    /**
     * 获得当前时间,到毫秒级别
     * @return
     */
    public static String getNowInSS() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }

    /**
     * 获得当前时间,以最常用的格式
     * @return
     */
    public static String getNowInCommon(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
        Date date=new Date();
        String date1=sf.format(date);
        return date1;
    }

    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 日期向后顺延
     * @param date
     * @param postpone 秒为单位
     * @return
     */
    public static Date datePostpone(Date date , Integer postpone){
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date);                  //需要将date数据转移到Calender对象中操作
        calendar.add(calendar.SECOND, postpone); //把日期往后增加以秒为单位.正数往后推,负数往前移动
        return calendar.getTime();               //这个时间就是日期往后推一天的结果
    }

    /**
     * 日期向前顺延
     * @param date
     * @param postpone
     * @return
     */
    public static String dateBeforePostpone(Date date , Integer postpone){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date);                  //需要将date数据转移到Calender对象中操作
        calendar.add(Calendar.MONTH, postpone); //把日期往后增加以秒为单位.正数往后推,负数往前移动
        return sdf.format(calendar.getTime());               //这个时间就是日期往前推一个月的结果
    }


    public static String formatDate(String timeParam) {

        long parameter = DateUtil.parse(timeParam).getTime();

//        获取当前时间
        DateTime nowDate = DateUtil.date();
        long nowDateLong = DateUtil.date().getTime();

        //获取一分钟后的时间
        long aMinuteLater = DateUtil.offset(nowDate, DateField.MINUTE, -1).getTime();

//        获取一小时后的时间
        long aHourLater = DateUtil.offset(nowDate, DateField.HOUR_OF_DAY, -1).getTime();

        //当前今天的时间
        Date todayDate = DateUtil.parse(DateUtil.date().toString(), "yyyy-MM-dd");
        long todayDateLong = todayDate.getTime();

        //获取昨天的时间
        long yesterdayDate = DateUtil.offset(todayDate, DateField.DAY_OF_MONTH, -1).getTime();

        //获取前天的时间
        long twoDayAfter = DateUtil.offset(todayDate, DateField.DAY_OF_MONTH, -2).getTime();

        //获得年的部分
        long year = DateUtil.parse(nowDate.toString(), "yyyy").getTime();

        String resultStr = "";
        if (parameter < nowDateLong & parameter > aMinuteLater) {
            resultStr = "刚刚";
            return resultStr;

        } else if (parameter >= aHourLater) {

            long betweenDay = DateUtil.between(new Date(parameter), nowDate, DateUnit.MINUTE);
            return betweenDay + "分钟前";

        } else if (parameter >= todayDateLong) {

            long betweenDay = DateUtil.between(new Date(parameter), nowDate, DateUnit.HOUR);
            resultStr = betweenDay + "小时前";
            return resultStr;

        } else if (parameter >= yesterdayDate) {

            String format = new SimpleDateFormat("HH:mm").format(new Date(parameter));
            resultStr = "昨天 " + format;
            return resultStr;

        } else if (parameter >= twoDayAfter) {

            String format = new SimpleDateFormat("HH:mm").format(new Date(parameter));
            resultStr = "前天 " + format;
            return resultStr;

        } else if (parameter <= year) {

//            不是今年
            resultStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(parameter));
            return resultStr;
        } else {
//            其他
            Date date1 = new Date(DateUtil.parse(timeParam).getTime());
            resultStr = new SimpleDateFormat("MM-dd HH:mm:ss").format(date1);

        }
        return resultStr;
    }
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        System.out.println(datePostpone(date,1*60));
        //System.out.println(getOrderIdByTime());
    }

}
