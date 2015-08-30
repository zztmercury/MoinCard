package com.lovemoin.card.app.db;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.utils.DateUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

/**
 * Entity mapped to table "CARD_INFO".
 */
public class CardInfo {

    // KEEP METHODS - put your custom methods here
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private Long id;
    /**
     * Not-null value.
     */
    private String cardCode;
    private String cardImg;
    private String cardBrand;
    /**
     * Not-null value.
     */
    private String cardType;
    /**
     * Not-null value.
     */
    private String cardDesc;
    /**
     * Not-null value.
     */
    private String convertObj;
    /**
     * Not-null value.
     */
    private java.util.Date createDate;
    /**
     * Not-null value.
     */
    private java.util.Date startDate;
    /**
     * Not-null value.
     */
    private java.util.Date endDate;
    private int convertPoint;
    private int currentPoint;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END
    private int maxPoint;

    public CardInfo() {
    }

    public CardInfo(Long id) {
        this.id = id;
    }

    public CardInfo(Long id, String cardCode, String cardImg, String cardBrand, String cardType, String cardDesc, String convertObj, java.util.Date createDate, java.util.Date startDate, java.util.Date endDate, int convertPoint, int currentPoint, int maxPoint) {
        this.id = id;
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
    }

    public CardInfo(JSONObject jsonObject) throws JSONException {
        cardCode = jsonObject.getString(Config.KEY_CARD_CODE);
        cardBrand = jsonObject.getString(Config.KEY_CARD_BRAND);
        cardDesc = jsonObject.getString(Config.KEY_CARD_DESC);
        cardImg = jsonObject.getString(Config.KEY_CARD_IMG);
        cardType = jsonObject.getString(Config.KEY_CARD_TYPE);
        convertObj = jsonObject.getString(Config.KEY_CONVERT_OBJ);
        convertPoint = jsonObject.getInt(Config.KEY_CONVERT_POINT);
        currentPoint = jsonObject.getInt(Config.KEY_CURRENT_POINT);
        maxPoint = jsonObject.getInt(Config.KEY_MAX_POINT);
        createDate = new Date(DateUtil.StringToLong(jsonObject.getString(Config.KEY_CREATE_DATE), DATE_PATTERN));
        startDate = new Date(DateUtil.StringToLong(jsonObject.getString(Config.KEY_START_DATE), DATE_PATTERN));
        endDate = new Date(DateUtil.StringToLong(jsonObject.getString(Config.KEY_END_DATE), DATE_PATTERN));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Not-null value.
     */
    public String getCardCode() {
        return cardCode;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
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

    /**
     * Not-null value.
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * Not-null value.
     */
    public String getCardDesc() {
        return cardDesc;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    /**
     * Not-null value.
     */
    public String getConvertObj() {
        return convertObj;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setConvertObj(String convertObj) {
        this.convertObj = convertObj;
    }

    /**
     * Not-null value.
     */
    public java.util.Date getCreateDate() {
        return createDate;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Not-null value.
     */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Not-null value.
     */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
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
    // KEEP METHODS END

}
