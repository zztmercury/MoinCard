package com.lovemoin.card.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zzt on 15-8-24.
 *
 * @author zzt
 *         日期辅助类
 */
public class DateUtil {
    public static Long StringToLong(String timeStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            return format.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0l;
        }
    }

    public static String LongToString(Long timeMills, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMills));
    }

    /**
     * 日期间隔
     *
     * @param date1 需对比的日期
     * @param date2 参照日期
     * @return date2 - date1 的天数
     */
    public static int dateDifference(Long date1, Long date2) {
        return Long.valueOf((long) Math.floor((date2 - date1) / (1000 * 60 * 60 * 24.0))).intValue();
    }
}
