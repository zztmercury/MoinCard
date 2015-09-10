package com.lovemoin.card.app.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "CARD_INFO".
 */
public class CardInfoDao extends AbstractDao<CardInfo, String> {

    public static final String TABLENAME = "CARD_INFO";
    private DaoSession daoSession;
    ;
    private Query<CardInfo> merchantInfo_CardInfoListQuery;
    private String selectDeep;

    public CardInfoDao(DaoConfig config) {
        super(config);
    }

    public CardInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CARD_INFO\" (" + //
                "\"CARD_CODE\" TEXT PRIMARY KEY NOT NULL ," + // 0: cardCode
                "\"CARD_IMG\" TEXT," + // 1: cardImg
                "\"CARD_BRAND\" TEXT," + // 2: cardBrand
                "\"CARD_TYPE\" TEXT NOT NULL ," + // 3: cardType
                "\"CARD_DESC\" TEXT NOT NULL ," + // 4: cardDesc
                "\"CONVERT_OBJ\" TEXT NOT NULL ," + // 5: convertObj
                "\"CREATE_DATE\" INTEGER NOT NULL ," + // 6: createDate
                "\"START_DATE\" INTEGER NOT NULL ," + // 7: startDate
                "\"END_DATE\" INTEGER NOT NULL ," + // 8: endDate
                "\"CONVERT_POINT\" INTEGER NOT NULL ," + // 9: convertPoint
                "\"CURRENT_POINT\" INTEGER NOT NULL ," + // 10: currentPoint
                "\"MAX_POINT\" INTEGER NOT NULL ," + // 11: maxPoint
                "\"MERCHANT_ID\" TEXT);"); // 12: merchantId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CARD_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CardInfo entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getCardCode());
 
        String cardImg = entity.getCardImg();
        if (cardImg != null) {
            stmt.bindString(2, cardImg);
        }
 
        String cardBrand = entity.getCardBrand();
        if (cardBrand != null) {
            stmt.bindString(3, cardBrand);
        }
        stmt.bindString(4, entity.getCardType());
        stmt.bindString(5, entity.getCardDesc());
        stmt.bindString(6, entity.getConvertObj());
        stmt.bindLong(7, entity.getCreateDate().getTime());
        stmt.bindLong(8, entity.getStartDate().getTime());
        stmt.bindLong(9, entity.getEndDate().getTime());
        stmt.bindLong(10, entity.getConvertPoint());
        stmt.bindLong(11, entity.getCurrentPoint());
        stmt.bindLong(12, entity.getMaxPoint());

        String merchantId = entity.getMerchantId();
        if (merchantId != null) {
            stmt.bindString(13, merchantId);
        }
    }

    @Override
    protected void attachEntity(CardInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public CardInfo readEntity(Cursor cursor, int offset) {
        CardInfo entity = new CardInfo( //
                cursor.getString(offset + 0), // cardCode
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cardImg
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // cardBrand
                cursor.getString(offset + 3), // cardType
                cursor.getString(offset + 4), // cardDesc
                cursor.getString(offset + 5), // convertObj
                new java.util.Date(cursor.getLong(offset + 6)), // createDate
                new java.util.Date(cursor.getLong(offset + 7)), // startDate
                new java.util.Date(cursor.getLong(offset + 8)), // endDate
                cursor.getInt(offset + 9), // convertPoint
                cursor.getInt(offset + 10), // currentPoint
                cursor.getInt(offset + 11), // maxPoint
                cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // merchantId
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CardInfo entity, int offset) {
        entity.setCardCode(cursor.getString(offset + 0));
        entity.setCardImg(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCardBrand(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCardType(cursor.getString(offset + 3));
        entity.setCardDesc(cursor.getString(offset + 4));
        entity.setConvertObj(cursor.getString(offset + 5));
        entity.setCreateDate(new java.util.Date(cursor.getLong(offset + 6)));
        entity.setStartDate(new java.util.Date(cursor.getLong(offset + 7)));
        entity.setEndDate(new java.util.Date(cursor.getLong(offset + 8)));
        entity.setConvertPoint(cursor.getInt(offset + 9));
        entity.setCurrentPoint(cursor.getInt(offset + 10));
        entity.setMaxPoint(cursor.getInt(offset + 11));
        entity.setMerchantId(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
    }

    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(CardInfo entity, long rowId) {
        return entity.getCardCode();
    }

    /** @inheritdoc */
    @Override
    public String getKey(CardInfo entity) {
        if (entity != null) {
            return entity.getCardCode();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Internal query to resolve the "cardInfoList" to-many relationship of MerchantInfo.
     */
    public List<CardInfo> _queryMerchantInfo_CardInfoList(String merchantId) {
        synchronized (this) {
            if (merchantInfo_CardInfoListQuery == null) {
                QueryBuilder<CardInfo> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.MerchantId.eq(null));
                merchantInfo_CardInfoListQuery = queryBuilder.build();
            }
        }
        Query<CardInfo> query = merchantInfo_CardInfoListQuery.forCurrentThread();
        query.setParameter(0, merchantId);
        return query.list();
    }

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getMerchantInfoDao().getAllColumns());
            builder.append(" FROM CARD_INFO T");
            builder.append(" LEFT JOIN MERCHANT_INFO T0 ON T.\"MERCHANT_ID\"=T0.\"MERCHANT_ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected CardInfo loadCurrentDeep(Cursor cursor, boolean lock) {
        CardInfo entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        MerchantInfo merchantInfo = loadCurrentOther(daoSession.getMerchantInfoDao(), cursor, offset);
        entity.setMerchantInfo(merchantInfo);

        return entity;
    }

    public CardInfo loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[]{key.toString()};
        Cursor cursor = db.rawQuery(sql, keyArray);

        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }

    /**
     * Reads all available rows from the given cursor and returns a list of new ImageTO objects.
     */
    public List<CardInfo> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<CardInfo> list = new ArrayList<CardInfo>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    protected List<CardInfo> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    /**
     * A raw-style query where you can pass any WHERE clause and arguments.
     */
    public List<CardInfo> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
    
/**
     * Properties of entity CardInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
 */
public static class Properties {
    public final static Property CardCode = new Property(0, String.class, "cardCode", true, "CARD_CODE");
    public final static Property CardImg = new Property(1, String.class, "cardImg", false, "CARD_IMG");
    public final static Property CardBrand = new Property(2, String.class, "cardBrand", false, "CARD_BRAND");
    public final static Property CardType = new Property(3, String.class, "cardType", false, "CARD_TYPE");
    public final static Property CardDesc = new Property(4, String.class, "cardDesc", false, "CARD_DESC");
    public final static Property ConvertObj = new Property(5, String.class, "convertObj", false, "CONVERT_OBJ");
    public final static Property CreateDate = new Property(6, java.util.Date.class, "createDate", false, "CREATE_DATE");
    public final static Property StartDate = new Property(7, java.util.Date.class, "startDate", false, "START_DATE");
    public final static Property EndDate = new Property(8, java.util.Date.class, "endDate", false, "END_DATE");
    public final static Property ConvertPoint = new Property(9, int.class, "convertPoint", false, "CONVERT_POINT");
    public final static Property CurrentPoint = new Property(10, int.class, "currentPoint", false, "CURRENT_POINT");
    public final static Property MaxPoint = new Property(11, int.class, "maxPoint", false, "MAX_POINT");
    public final static Property MerchantId = new Property(12, String.class, "merchantId", false, "MERCHANT_ID");
    }
 
}
