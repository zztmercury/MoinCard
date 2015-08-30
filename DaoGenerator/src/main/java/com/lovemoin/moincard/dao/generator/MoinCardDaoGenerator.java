package com.lovemoin.moincard.dao.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MoinCardDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.lovemoin.card.app.db");
        schema.enableKeepSectionsByDefault();
        initCardInfo(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void initCardInfo(Schema schema) {
        Entity cardInfo = schema.addEntity("CardInfo");
        cardInfo.addIdProperty().autoincrement();
        cardInfo.addStringProperty("cardCode").notNull();
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
    }

    private static void initMerchantInfo(Schema schema) {
        Entity merchantInfo = schema.addEntity("MerchantINfo");
        merchantInfo.addStringProperty("merchantId").primaryKey();
    }
}


