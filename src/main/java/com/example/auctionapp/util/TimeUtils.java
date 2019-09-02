package com.example.auctionapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
    /**
     * 取得系统时间
     * @param pattern eg:yyyy-MM-dd HH:mm:ss,SSS
     * @return
     */
    public static String getSysTime(String pattern) {

        return formatSysTime(new SimpleDateFormat(pattern));
    }

    /**
     * 格式化系统时间
     * @param format
     * @return
     */
    private static String formatSysTime(SimpleDateFormat format) {

        String str = format.format(Calendar.getInstance().getTime());
        return str;
    }
}
