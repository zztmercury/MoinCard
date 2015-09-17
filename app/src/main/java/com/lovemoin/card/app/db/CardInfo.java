package com.lovemoin.card.app.db;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.utils.DateUtil;
import de.greenrobot.dao.DaoException;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS
// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "CARD_INFO".
 */
public class CardInfo implements java.io.Serializable {

    /**
     * Not-null value.
     */
    private String cardCode;
    private String cardImg;
    private String cardBrand;
    /** Not-null value. */
    private String cardType;
    /** Not-null value. */
    private String cardDesc;
    /** Not-null value. */
    private String convertObj;
    /** Not-null value. */
    private java.util.Date createDate;
    /** Not-null value. */
    private java.util.Date startDate;
    /** Not-null value. */
    private java.util.Date endDate;
    private int convertPoint;
    private int currentPoint;
    private int maxPoint;
    private String merchantId;
    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    private transient CardInfoDao myDao;
    private MerchantInfo merchantInfo;
    private String merchantInfo__resolvedKey;
    // KEEP FIELDS - put your custom fields here
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 兑换卡
     */
    public static final String TYPE_COUPON = "0001";
    /**
     * 积点卡
     */
    public static final String TYPE_POINT = "0000";
    // KEEP FIELDS END

    public CardInfo() {
    }

    public CardInfo(String cardCode) {
        this.cardCode = cardCode;
    }

    public CardInfo(String cardCode, String cardImg, String cardBrand, String cardType, String cardDesc, String convertObj, java.util.Date createDate, java.util.Date startDate, java.util.Date endDate, int convertPoint, int currentPoint, int maxPoint, String merchantId) {
        this.cardCode = cardCode;
        this.cardImg = cardImg;
        this.cardBrand = cardBrand;
        this.cardType = cardType;
        this.cardDesc = cardDesc;
        this.convertObj = convertObj;
        this.createDate = createDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.convertPoint = convertPoint;
        this.currentPoint = currentPoint;
        this.maxPoint = maxPoint;
        this.merchantId = merchantId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCardInfoDao() : null;
    }

    /** Not-null value. */
    public String getCardCode() {
        return cardCode;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    /** Not-null value. */
    public String getCardType() {
        return cardType;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /** Not-null value. */
    public String getCardDesc() {
        return cardDesc;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    /** Not-null value. */
    public String getConvertObj() {
        return convertObj;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setConvertObj(String convertObj) {
        this.convertObj = convertObj;
    }

    /** Not-null value. */
    public java.util.Date getCreateDate() {
        return createDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /** Not-null value. */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    /** Not-null value. */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public int getConvertPoint() {
        return convertPoint;
    }

    public void setConvertPoint(int convertPoint) {
        this.convertPoint = convertPoint;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    public int getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public MerchantInfo getMerchantInfo() {
        String __key = this.merchantId;
        if (merchantInfo__resolvedKey == null || merchantInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MerchantInfoDao targetDao = daoSession.getMerchantInfoDao();
            MerchantInfo merchantInfoNew = targetDao.load(__key);
            synchronized (this) {
                merchantInfo = merchantInfoNew;
                merchantInfo__resolvedKey = __key;
            }
        }
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        synchronized (this) {
            this.merchantInfo = merchantInfo;
            merchantId = merchantInfo == null ? null : merchantInfo.getMerchantId();
            merchantInfo__resolvedKey = merchantId;
        }
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public CardInfo(JSONObject jsonObject) throws JSONException {
        cardCode = jsonObject.getString(Config.KEY_CARD_CODE);
        cardImg = jsonObject.getString(Config.KEY_CARD_IMG);
        cardBrand = jsonObject.getString(Config.KEY_CARD_BRAND);
        cardType = jsonObject.getString(Config.KEY_CARD_TYPE);
        cardDesc = jsonObject.getString(Config.KEY_CARD_DESC);
        convertObj = jsonObject.getString(Config.KEY_CONVERT_OBJ);
        createDate = new Date(DateUtil.StringToLong(jsonObject.getString(Config.KEY_CREATE_DATE), DATE_PATTERN));
        startDate = new Date(DateUtil.StringToLong(jsonObject.getString(Config.KEY_START_DATE), DATE_PATTERN));
        endDate = new Date(DateUtil.StringToLong(jsonObject.getString(Config.KEY_END_DATE), DATE_PATTERN));
        convertPoint = jsonObject.getInt(Config.KEY_CONVERT_POINT);
        currentPoint = jsonObject.getInt(Config.KEY_CURRENT_POINT);
        maxPoint = jsonObject.getInt(Config.KEY_MAX_POINT);
    }
    // KEEP METHODS END

}
