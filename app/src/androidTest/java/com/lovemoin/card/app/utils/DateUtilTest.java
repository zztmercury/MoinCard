package com.lovemoin.card.app.utils;

import junit.framework.TestCase;

/**
 * Created by zzt on 15-8-25.
 */
public class DateUtilTest extends TestCase {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private String dateStr = "2015-07-03";
    private String dateStr2 = "2015-07-08";

//    public void testStringToLong() throws Exception {
//        assertEquals(DateUtil.StringToLong(dateStr, DATE_PATTERN).longValue(), new Date(2015, 7, 3).getTime());
//    }
//
//    public void testLongToString() throws Exception {
//        assertEquals(new Date(2015, 7, 3).getTime(), DateUtil.StringToLong(dateStr, DATE_PATTERN).longValue());
//    }
//
//    public void testDateDifference() throws Exception {
//        long l = new Date(2015, 7, 3).getTime();
//        assertTrue(DateUtil.dateDifference(l, l - 60 * 60 * 24) < 0);
//    }

    public void test() throws Exception {
        int i = 16;
        String result = CommonUtil.padLeft(Integer.toHexString(i), 4);
        assertEquals("0010",result);
    }
}