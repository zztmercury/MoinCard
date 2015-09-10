package com.lovemoin.card.app.constant;

/**
 * Created by zzt on 15-8-24.
 */
public class Config {
    // 服务器通信配置
    /**
     * 服务器地址<br/>
     * 测试服务器 http://182.92.3.209:8080/moinbox/<br/>
     * 生产服务器 <br/>
     * 开发服务器 http://192.168.2.118:8080/moinbox/
     */
    public static final String SERVER_URL = "http://192.168.2.118:8080/moinbox/";
    public static final String CHARSET = "UTF-8";

    // 通信接口配置
    public static final String ACTION_LOGIN = "cardUserActionAjax/allMethod!loginCardUser.action";
    public static final String ACTION_LOAD_CARD_LIST = "cardUserActionAjax/allMethod!queryCardUserRefPointCard.action";
    public static final String ACTION_REGISTER = "cardUserActionAjax/allMethod!registCardUser.action";
    public static final String ACTION_LOAD_ACTIVITY_LIST = "activityActionAjax/activityInfo!findByUser.action";
    public static final String ACTION_ATTEND_ACTIVITY = "activityActionAjax/activityInfo!attend.action";
    public static final String ACTION_LOAD_MERCHANT_INFO = "merchantActionAjax/merchantInfo!findMerchant.action";
    public static final String ACTION_LOAD_ACTIVITY_DETAIL = "activityActionAjax/activityInfo!detail.action";

    // 卡相关key
    public static final String KEY_CARD = "card";
    public static final String KEY_CARD_CODE = "cardId";
    public static final String KEY_CARD_IMG = "imgFlag";
    public static final String KEY_CONVERT_OBJ = "convertName";
    public static final String KEY_CREATE_DATE = "createDate";
    public static final String KEY_CARD_BRAND = "brand";
    public static final String KEY_CONVERT_POINT = "convertPoint";
    public static final String KEY_CURRENT_POINT = "currentPoint";
    public static final String KEY_MAX_POINT = "maxPoint";
    public static final String KEY_CARD_TYPE = "pointCardType";
    public static final String KEY_CARD_DESC = "comment";

    // 商户相关key
    public static final String KEY_MERCHANT_NAME = "merchantName";
    public static final String KEY_MERCHANT_ID = "merchantId";
    public static final String KEY_MERCHANT_BRAND = "brand";
    public static final String KEY_MERCHANT_MAIN_IMAGE = "mainImg";
    public static final String KEY_MERCHANT_BRIEF = "brief";
    public static final String KEY_MERCHANT_DESCRIPTION = "desc";


    // 用户相关key
    public static final String KEY_USER_TEL = "userTel";
    public static final String KEY_USER_PASSWORD = "userPwd";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_OS_VERSION = "OSVersion";
    public static final String KEY_MODEL = "model";

    // 服务器返回结果key
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PARAM = "param";

    // 活动相关key
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_ACTIVITY_ID = "activityId";
    public static final String KEY_ACTIVITY_NAME = "name";
    public static final String KEY_ACTIVITY_BRIEF = "brief";
    public static final String KEY_ACTIVITY_BRIEF_IMAGE = "briefImg";
    public static final String KEY_ACTIVITY_TYPE = "type";
    public static final String KEY_IS_OFFICIAL = "isOfficial";
    public static final String KEY_IS_ATTEND = "isAttend";
    public static final String KEY_IS_TOP = "isTop";
    public static final String KEY_ACTIVITY_DETAIL = "detail";
    public static final String KEY_ACTIVITY_DETAIL_IMAGE = "img";
    public static final String KEY_VIEW_TYPE = "viewType";
    public static final String KEY_ACTIVITY_NUM = "num";
    public static final String KEY_ACTIVITY_MERCHANT = "merchant";
    public static final String KEY_ACTIVITY_STEP_TARGET = "stepTarget";
    public static final String KEY_ACTIVITY_STEP = "step";
    public static final String KEY_ACTIVITY_TOTAL_STEP = "totalStep";
    public static final String KEY_ACTIVITY_STEP_TEXT = "stepText";
    public static final String KEY_ACTIVITY_PROGRESS = "progress";
    public static final String KEY_ACTIVITY_MEMBER_COUNT = "memberCount";

    public static final String KEY_LAST_SEARCH_TIME = "lastSearchTime";

    // 通用key
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_END_DATE = "endDate";
}