package com.lovemoin.moincard.dao.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

import java.io.IOException;

public class MoinCardDaoGenerator extends DaoGenerator {
    private Schema schema;
    private Entity cardInfo;
    private Entity merchantInfo;
    private Entity activityInfo;
    private Entity imageInfo;
    private Entity storeInfo;

    private Property pkCardInfo;
    private Property pkActivityInfo;
    private Property pkImageInfo;
    private Property pkStoreInfo;

    private Property fkCardInfo;
    private Property fkActivityInfo;
    private Property fkImageInfo;
    private Property fkStoreInfo;


    public MoinCardDaoGenerator() throws IOException {
        schema = new Schema(3, "com.lovemoin.card.app.db");
        schema.enableKeepSectionsByDefault();
        initCardInfo();
        initMerchantInfo();
        initImageInfo();
        initStoreInfo();
        initActivityInfo();
        initRelation();
    }

    public static void main(String[] args) throws Exception {
        new MoinCardDaoGenerator().generateAll();

    }

    public void generateAll() throws Exception {
        super.generateAll(schema, "./app/src/main/java");
    }

    private void initCardInfo() {
        cardInfo = schema.addEntity("CardInfo");
        cardInfo.implementsSerializable();
        pkCardInfo = cardInfo.addStringProperty("cardCode").notNull().primaryKey().getProperty();
        cardInfo.addStringProperty("cardName");
        cardInfo.addStringProperty("cardImg");
        cardInfo.addStringProperty("cardBrand");
        cardInfo.addStringProperty("cardType").notNull();
        cardInfo.addStringProperty("cardDesc").notNull();
        cardInfo.addStringProperty("convertObj").notNull();
        cardInfo.addDateProperty("createDate").notNull();
        cardInfo.addDateProperty("startDate").notNull();
        cardInfo.addDateProperty("endDate").notNull();
        cardInfo.addIntProperty("convertPoint").notNull();
        cardInfo.addIntProperty("currentPoint").notNull();
        cardInfo.addIntProperty("maxPoint").notNull();
        fkCardInfo = cardInfo.addStringProperty("merchantId").getProperty();
    }

    private void initMerchantInfo() {
        merchantInfo = schema.addEntity("MerchantInfo");
        merchantInfo.addStringProperty("merchantUUID").primaryKey();
        merchantInfo.addStringProperty("merchantId");
        merchantInfo.addStringProperty("merchantName").notNull();
        merchantInfo.addStringProperty("brand").notNull();
        merchantInfo.addStringProperty("mainImg");
        merchantInfo.addStringProperty("brief");
        merchantInfo.addStringProperty("description");
    }

    private void initActivityInfo() {
        activityInfo = schema.addEntity("ActivityInfo");
        activityInfo.implementsSerializable();
        pkActivityInfo = activityInfo.addStringProperty("activityId").primaryKey().getProperty();
        activityInfo.addStringProperty("name").notNull();
        activityInfo.addStringProperty("brief").notNull();
        activityInfo.addStringProperty("address");
        activityInfo.addIntProperty("level");
        activityInfo.addStringProperty("detail");
        activityInfo.addStringProperty("img");
        activityInfo.addStringProperty("briefImg");
        activityInfo.addDateProperty("startDate").notNull();
        activityInfo.addDateProperty("endDate").notNull();
        activityInfo.addBooleanProperty("isTop");
        activityInfo.addBooleanProperty("isOfficial");
        activityInfo.addBooleanProperty("isAttend");
        activityInfo.addBooleanProperty("isViewed");
        activityInfo.addIntProperty("type").notNull();
        fkActivityInfo = activityInfo.addStringProperty("merchantId").getProperty();
    }

    private void initImageInfo() {
        imageInfo = schema.addEntity("ImageInfo");
        pkImageInfo = imageInfo.addIdProperty().autoincrement().primaryKey().getProperty();
        imageInfo.addIntProperty("width").notNull();
        imageInfo.addIntProperty("height").notNull();
        imageInfo.addIntProperty("color").notNull();
        imageInfo.addStringProperty("url").notNull();
        imageInfo.addStringProperty("desc");
        fkImageInfo = imageInfo.addStringProperty("merchantId").getProperty();
    }

    private void initStoreInfo() {
        storeInfo = schema.addEntity("StoreInfo");
        pkStoreInfo = storeInfo.addStringProperty("storeId").primaryKey().getProperty();
        storeInfo.addStringProperty("name").notNull();
        storeInfo.addStringProperty("addr");
        storeInfo.addStringProperty("tel");
        fkStoreInfo = storeInfo.addStringProperty("merchantId").getProperty();
    }

    private void initRelation() {
        cardInfo.addToOne(merchantInfo, fkCardInfo);
        imageInfo.addToOne(merchantInfo, fkImageInfo);
        activityInfo.addToOne(merchantInfo, fkActivityInfo);
        storeInfo.addToOne(merchantInfo, fkStoreInfo);
        merchantInfo.addToMany(cardInfo, fkCardInfo);
        merchantInfo.addToMany(imageInfo, fkImageInfo);
        merchantInfo.addToMany(activityInfo, fkActivityInfo);
        merchantInfo.addToMany(storeInfo, fkStoreInfo);
    }
}

