package com.lovemoin.card.app.constant;

/**
 * Created by zzt on 15-8-24.
 */
public class Config {
    public static final String APP_NAME = "moinCard";
    public static final String KEY_LOGIN_STATUS = "loginStatus";
    // 服务器通信配置
    public static final String SERVER_URL = "http://182.92.3.209:8080/moinbox/";
    public static final String CHARSET = "UTF-8";

    // 通信接口配置
    public static final String ACTION_LOGIN = "cardUserActionAjax/allMethod!loginCardUser.action";
    public static final String ACTION_DOWNLOAD_CARD_LIST = "cardUserActionAjax/allMethod!queryCardUserRefPointCard.action";

    // 卡相关key
    public static final String KEY_CARD_CODE = "cardId";
    public static final String KEY_CARD_IMG = "imgFlag";
    public static final String KEY_CONVERT_OBJ = "convertName";
    public static final String KEY_CREATE_DATE = "createDate";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_END_DATE = "endDate";
    public static final String KEY_CARD_BRAND = "brand";
    public static final String KEY_CONVERT_POINT = "convertPoint";
    public static final String KEY_CURRENT_POINT = "currentPoint";
    public static final String KEY_MAX_POINT = "maxPoint";
    public static final String KEY_CARD_TYPE = "pointCardType";
    public static final String KEY_CARD_DESC = "comment";

    // 用户相关key
    public static final String KEY_USER_TEL = "userTel";
    public static final String KEY_USER_PASSWORD = "userPwd";
    public static final String KEY_USER_ID = "userId";

    // 服务器返回结果key
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PARAM = "param";
}