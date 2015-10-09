package com.lovemoin.card.app.entity;

/**
 * Created by zzt on 15-9-15.
 */
public class DeviceInfo {
    /**
     * 等待状态
     */
    public static final int OPER_WAITE = 0x0;
    /**
     * 积点设备状态
     */
    public static final int OPER_PLUS = 0x1;
    /**
     * 兑换设备状态
     */
    public static final int OPER_CONVERT = 0x2;
    /**
     * 签到设备状态
     */
    public static final int OPER_SIGN = 0x3;
    /**
     * 卡验证设备状态
     */
    public static final int OPER_CARD_CONFIRM = 0x4;
    private String deviceId;
    private int point;
    private int oper;
    private int count;

    public DeviceInfo(String s) {
        deviceId = s.substring(0, 32);
        point = Integer.valueOf(s.substring(32, 34), 16);
        oper = Integer.valueOf(s.substring(34, 36), 16);
        count = Integer.valueOf(s.substring(36, 44), 16);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getPoint() {
        return point;
    }

    public int getOper() {
        return oper;
    }

    public int getCount() {
        return count;
    }

    public String getMerchantCode() {
        return deviceId.substring(0, 12);
    }
}
