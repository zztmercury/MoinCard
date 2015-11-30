package com.lovemoin.card.app.constant;

/**
 * Created by zzt on 15-8-24.
 */
public class Config {
    // 服务器通信配置
    public static final String CHARSET = "UTF-8";
    // 通信接口配置
    public static final String ACTION_LOGIN = "/moinbox/cardUserActionAjax/allMethod!loginCardUser.action";
    public static final String ACTION_LOAD_CARD_LIST = "/moinbox/cardUserActionAjax/allMethod!queryCardUserRefPointCard.action";
    public static final String ACTION_REGISTER = "/moinbox/cardUserActionAjax/allMethod!registCardUser.action";
    public static final String ACTION_LOAD_ACTIVITY_LIST = "/moinbox/activityActionAjax/activityInfo!findByUser.action";
    public static final String ACTION_ATTEND_ACTIVITY = "/moinbox/activityActionAjax/activityInfo!attend.action";
    public static final String ACTION_LOAD_MERCHANT_INFO = "/moinbox/merchantActionAjax/merchantInfo!findMerchant.action";
    public static final String ACTION_LOAD_ACTIVITY_DETAIL = "/moinbox/activityActionAjax/activityInfo!detail.action";
    public static final String ACTION_GET_PRIZE = "/moinbox/activityActionAjax/activityInfo!getThePrize.action";
    public static final String ACTION_GET_RELATE_MERCHANT_COUPON = "/moinbox/activityActionAjax/activityInfo!getMerchantAndPointCard.action";
    public static final String ACTION_LOAD_CARD_RECORD = "/moinbox/cardUserHisActionAjax/ajax!queryCardHisListByAjax.action";
    public static final String ACTION_LOAD_STORE_LIST = "/moinbox/merchantStoreActionAjax/merchantStoreInfo!findStore.action";
    public static final String ACTION_LOAD_MERCHANT_IMAGE_SET = "/moinbox/merchantActionAjax/merchantInfo!findImgList.action";
    public static final String ACTION_CREATE_CARD = "/moinbox/cardUserActionAjax/allMethod!createCard2CardUser.action";
    public static final String ACTION_GET_POINT = "/moinbox/cardAction/decipheringMessage.action";
    public static final String ACTION_SIGN_IN = "/moinbox/cardAction/decipheringMessageOfSignIn.action";
    public static final String ACTION_QUICK_EXCHANGE = "/moinbox/cardAction/quickExchange.action";
    public static final String ACTION_SEND_CODE = "/moinbox/cardUserActionAjax/allMethod!sendMobileVerificationCode.action";
    public static final String ACTION_DELETE_CARD = "/moinbox/cardUserActionAjax/allMethod!deleteCardUserRefCard.action";
    public static final String ACTION_LOAD_ACTIVITY_LIST_BY_MERCHANT = "/moinbox/activityActionAjax/activityInfo!findByUserAndMerchantId.action";
    public static final String ACTION_MODIFY_USER = "/moinbox/cardUserActionAjax/allMethod!modifyCardUserPwd.action";
    public static final String ACTION_CHECK_VERSION = "/moinbox/cardUserActionAjax/allMethod!checkVersionAjax.action";
    public static final String ACTION_GET_GIFT_FOR_NEW_USER_BY_CODE = "/moinbox/discountActionAjax/getGiftForNewUserByCode.action";
    public static final String ACTION_LOAD_GIFT_PACK_LIST = "/moinbox/discountActionAjax/getList.action";
    public static final String ACTION_GET_GIFT = "/moinbox/discountActionAjax/getGift.action";
    public static final String ACTION_HAS_NUM_1_ACT = "/moinbox/activityActionAjax/activityInfo!hasNum1.action";
    public static final String ACTION_LOGIN_BY_IMEI = "/moinbox/cardUserActionAjax/allMethod!loginByIMEI.action";
    public static final String ACTION_LOGIN_BY_WE_CHAT = "/moinbox/cardUserActionAjax/allMethod!loginByWechat.action";
    public static final String ACTION_GET_HELP = "/moinbox/messageActionAjax/getHelp.action";
    public static final String ACTIOB_LOAD_AD_LIST = "/moinbox/activityActionAjax/getAdListAjax.action";
    public static final String ACTION_SEND_SHARE_LOG = "/moinbox/weChantActionAjax/shareResponseAjax.action";
    // 卡相关key
    public static final String KEY_CARD = "card";
    public static final String KEY_CARD_CODE = "cardId";
    public static final String KEY_CARD_NUMBER = "cardNumber";
    public static final String KEY_CARD_IMG = "imgFlag";
    public static final String KEY_CONVERT_OBJ = "convertName";
    public static final String KEY_CREATE_DATE = "createDate";
    public static final String KEY_CARD_BRAND = "brand";
    public static final String KEY_CONVERT_POINT = "convertPoint";
    public static final String KEY_CURRENT_POINT = "currentPoint";
    public static final String KEY_MAX_POINT = "maxPoint";
    public static final String KEY_CARD_TYPE = "pointCardType";
    public static final String KEY_CARD_DESC = "comment";
    public static final String KEY_POINT_CARD_ID = "pointCardId";
    public static final String KEY_COUPON_NAME = "pointCardName";
    // 商户相关key
    public static final String KEY_MERCHANT = "merchant";
    public static final String KEY_MERCHANT_NAME = "merchantName";
    public static final String KEY_MERCHANT_ID = "merchantId";
    public static final String KEY_MERCHANT_UUID = "merchantUUID";
    public static final String KEY_MERCHANT_BRAND = "brand";
    public static final String KEY_MERCHANT_MAIN_IMAGE = "mainImg";
    public static final String KEY_MERCHANT_BRIEF = "brief";
    public static final String KEY_MERCHANT_DESCRIPTION = "desc";
    // 门店相关key
    public static final String KEY_STORE_ID = "id";
    public static final String KEY_STORE_NAME = "name";
    public static final String KEY_STORE_ADDR = "addr";
    public static final String KEY_STORE_TEL = "tel";
    // 商户图片相关key
    public static final String KEY_MERCHANT_IMG_URL = "url";
    public static final String KEY_MERCHANT_IMG_HEIGHT = "height";
    public static final String KEY_MERCHANT_IMG_WIDTH = "width";
    public static final String KEY_MERCHANT_IMG_DESCRIPTION = "desc";
    public static final String KEY_MERCHANT_IMG_COLOR = "color";
    // 用户相关key
    public static final String KEY_USER_TEL = "userTel";
    public static final String KEY_USER_PASSWORD = "userPwd";
    public static final String KEY_NEW_PASSWORD = "userPwd";
    public static final String KEY_OLD_PASSWORD = "oldPwd";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_CARD_USER_ID = "cardUserId";
    public static final String KEY_OS_VERSION = "OSVersion";
    public static final String KEY_MODEL = "model";
    public static final String KEY_CHECK_CODE = "checkCode";
    public static final String KEY_USER_IMG = "userImg";
    public static final String KEY_USERNAME = "userName";
    // 服务器返回结果key
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PARAM = "param";
    // 活动相关key
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_ACTIVITY_ID = "activityId";
    public static final String KEY_ACTIVITY_ADDR = "address";
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
    public static final String KEY_ACTIVITY_LEVEL = "level";
    public static final String KEY_LAST_SEARCH_TIME = "lastSearchTime";
    public static final String KEY_GIFT_IMG = "giftImg";
    public static final String KEY_GIFT_NAME = "giftName";
    public static final String KEY_SHARE_URL = "shareUrl";
    // 记录相关key
    public static final String KEY_OPERATE_DATE = "operateDate";
    public static final String KEY_OPERATE_TYPE = "operateType";
    public static final String KEY_OPERATE_POINT = "point";
    // 礼包key
    public static final String KEY_GIFT_PACK = "gift_pack";
    public static final String KEY_GIFT_CODE = "code";
    public static final String KEY_GIFT_ID = "giftId";
    public static final String KEY_GIFT_PRIORITY = "level";
    public static final String KEY_GIFT_COMMENT = "comment";
    public static final String KEY_GIFT_IGNORE = "ignore";
    public static final String KEY_NEED_CODE = "needCode";
    public static final String KEY_POINT_ADD_NUM = "pointAddNum";
    // 通用key
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_END_DATE = "endDate";
    public static final String KEY_CHECK_VALUE = "checkValue";
    public static final String KEY_VERSION = "version";
    public static final String KEY_DEVICE_ID = "IMEI";
    public static final String KEY_CODE = "code";
    public static final String KEY_URL = "url";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_TITLE = "title";

    // 微信配置
    public static final String WE_CHAT_APP_ID = "wxb15c815e08b65acc";

    /**
     * 服务器地址<br/>
     * 测试服务器 http://182.92.3.209:8080<br/>
     * 生产服务器 http://www.lovemoin.com<br/>
     * 开发服务器 http://192.168.2.118:8080
     */
    public static String SERVER_URL = "http://www.lovemoin.com";

}