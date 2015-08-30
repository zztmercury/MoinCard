package com.lovemoin.card.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zzt on 15-8-24.
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

    public static int dateDifference(Long date1, Long date2) {
        return new Long((date2 - date1) / (1000 * 60 * 60 * 24)).intValue();
    }
}
