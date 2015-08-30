package com.lovemoin.card.app.utils;

import junit.framework.TestCase;

/**
 * Created by zzt on 15-8-25.
 */
public class DateUtilTest extends TestCase {

    public void testStringToLong() throws Exception {
        String dateStr = "2015-07-03";
        String dateStr2 = "2015-07-08";
        assertEquals(DateUtil.dateDifference(DateUtil.StringToLong(dateStr, "yyyy-MM-dd"), DateUtil.StringToLong(dateStr2, "yyyy-MM-dd")), 5);
    }
}