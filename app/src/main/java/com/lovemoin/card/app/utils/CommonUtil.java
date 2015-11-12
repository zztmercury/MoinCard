package com.lovemoin.card.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zzt on 15-8-24.
 */
public class CommonUtil {

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /***
     * 是否为空
     *
     * @param str
     * @return 为空返回true
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.equals("") || str.equalsIgnoreCase("null")
                || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static String ByteArrayToHexString(byte[] bytes) {
        //final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
        //        '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        //char[] hexChars = new char[bytes.length * 2];
        //int v;
        //for (int j = 0; j < bytes.length; j++) {
        //    v = bytes[j] & 0xFF;
        //    hexChars[j * 2] = hexArray[v >>> 4];
        //    hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        //}
        //return new String(hexChars);
        StringBuffer buf = new StringBuffer();
        for (byte b :
                bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1)
                buf.append(0);
            buf.append(hex);
        }
        return buf.toString();
    }

    public static byte[] HexStringToByteArray(String s) {
        //int len = s.length();
        //byte[] data = new byte[len / 2];
        //for (int i = 0; i < len; i += 2) {
        //    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
        //            .digit(s.charAt(i + 1), 16));
        //}
        //return data;
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < s.length() / 2; i++) {
            int j = Integer.valueOf(s.substring(i * 2, (i + 1) * 2), 16);
            bytes[i] = (byte) (j & 0xff);
        }
        return bytes;
    }

    public static byte xor(byte[] tempbuf, int len) {
        // TODO Auto-generated method stub
        int i;
        byte result = 0;
        for (i = 0; i < len; i++) {
            result = (byte) (result ^ tempbuf[i]);
        }
        return result;
    }

    public static String padLeft(String s, int length) {
        byte[] bs = new byte[length];
        byte[] ss = s.getBytes();
        Arrays.fill(bs, (byte) (48 & 0xff));
        System.arraycopy(ss, 0, bs, length - ss.length, ss.length);
        return new String(bs);
    }


}
