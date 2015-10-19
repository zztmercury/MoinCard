package com.lovemoin.card.app.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.lovemoin.card.app.db.ActivityInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ACTIVITY_INFO".
*/
public class ActivityInfoDao extends AbstractDao<ActivityInfo, String> {

    public static final String TABLENAME = "ACTIVITY_INFO";

    /**
     * Properties of entity ActivityInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ActivityId = new Property(0, String.class, "activityId", true, "ACTIVITY_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Brief = new Property(2, String.class, "brief", false, "BRIEF");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property Level = new Property(4, Integer.class, "level", false, "LEVEL");
        public final static Property Detail = new Property(5, String.class, "detail", false, "DETAIL");
        public final static Property Img = new Property(6, String.class, "img", false, "IMG");
        public final static Property BriefImg = new Property(7, String.class, "briefImg", false, "BRIEF_IMG");
        public final static Property StartDate = new Property(8, java.util.Date.class, "startDate", false, "START_DATE");
        public final static Property EndDate = new Property(9, java.util.Date.class, "endDate", false, "END_DATE");
        public final static Property IsTop = new Property(10, Boolean.class, "isTop", false, "IS_TOP");
        public final static Property IsOfficial = new Property(11, Boolean.class, "isOfficial", false, "IS_OFFICIAL");
        public final static Property IsAttend = new Property(12, Boolean.class, "isAttend", false, "IS_ATTEND");
        public final static Property IsViewed = new Property(13, Boolean.class, "isViewed", false, "IS_VIEWED");
        public final static Property Type = new Property(14, int.class, "type", false, "TYPE");
        public final static Property Num = new Property(15, Integer.class, "num", false, "NUM");
        public final static Property MerchantId = new Property(16, String.class, "merchantId", false, "MERCHANT_ID");
    };

    private DaoSession daoSession;

    private Query<ActivityInfo> merchantInfo_ActivityInfoListQuery;

    public ActivityInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ActivityInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACTIVITY_INFO\" (" + //
                "\"ACTIVITY_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: activityId
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"BRIEF\" TEXT NOT NULL ," + // 2: brief
                "\"ADDRESS\" TEXT," + // 3: address
                "\"LEVEL\" INTEGER," + // 4: level
                "\"DETAIL\" TEXT," + // 5: detail
                "\"IMG\" TEXT," + // 6: img
                "\"BRIEF_IMG\" TEXT," + // 7: briefImg
                "\"START_DATE\" INTEGER NOT NULL ," + // 8: startDate
                "\"END_DATE\" INTEGER NOT NULL ," + // 9: endDate
                "\"IS_TOP\" INTEGER," + // 10: isTop
                "\"IS_OFFICIAL\" INTEGER," + // 11: isOfficial
                "\"IS_ATTEND\" INTEGER," + // 12: isAttend
                "\"IS_VIEWED\" INTEGER," + // 13: isViewed
                "\"TYPE\" INTEGER NOT NULL ," + // 14: type
                "\"NUM\" INTEGER," + // 15: num
                "\"MERCHANT_ID\" TEXT);"); // 16: merchantId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACTIVITY_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ActivityInfo entity) {
        stmt.clearBindings();
 
        String activityId = entity.getActivityId();
        if (activityId != null) {
            stmt.bindString(1, activityId);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getBrief());
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(5, level);
        }
 
        String detail = entity.getDetail();
        if (detail != null) {
            stmt.bindString(6, detail);
        }
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(7, img);
        }
 
        String briefImg = entity.getBriefImg();
        if (briefImg != null) {
            stmt.bindString(8, briefImg);
        }
        stmt.bindLong(9, entity.getStartDate().getTime());
        stmt.bindLong(10, entity.getEndDate().getTime());
 
        Boolean isTop = entity.getIsTop();
        if (isTop != null) {
            stmt.bindLong(11, isTop ? 1L: 0L);
        }
 
        Boolean isOfficial = entity.getIsOfficial();
        if (isOfficial != null) {
            stmt.bindLong(12, isOfficial ? 1L: 0L);
        }
 
        Boolean isAttend = entity.getIsAttend();
        if (isAttend != null) {
            stmt.bindLong(13, isAttend ? 1L: 0L);
        }
 
        Boolean isViewed = entity.getIsViewed();
        if (isViewed != null) {
            stmt.bindLong(14, isViewed ? 1L: 0L);
        }
        stmt.bindLong(15, entity.getType());
 
        Integer num = entity.getNum();
        if (num != null) {
            stmt.bindLong(16, num);
        }
 
        String merchantId = entity.getMerchantId();
        if (merchantId != null) {
            stmt.bindString(17, merchantId);
        }
    }

    @Override
    protected void attachEntity(ActivityInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ActivityInfo readEntity(Cursor cursor, int offset) {
        ActivityInfo entity = new ActivityInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // activityId
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // brief
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // level
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // detail
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // img
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // briefImg
            new java.util.Date(cursor.getLong(offset + 8)), // startDate
            new java.util.Date(cursor.getLong(offset + 9)), // endDate
            cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0, // isTop
            cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0, // isOfficial
            cursor.isNull(offset + 12) ? null : cursor.getShort(offset + 12) != 0, // isAttend
            cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0, // isViewed
            cursor.getInt(offset + 14), // type
            cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15), // num
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16) // merchantId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ActivityInfo entity, int offset) {
        entity.setActivityId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setBrief(cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLevel(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setDetail(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setImg(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setBriefImg(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStartDate(new java.util.Date(cursor.getLong(offset + 8)));
        entity.setEndDate(new java.util.Date(cursor.getLong(offset + 9)));
        entity.setIsTop(cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0);
        entity.setIsOfficial(cursor.isNull(offset + 11) ? null : cursor.getShort(offset + 11) != 0);
        entity.setIsAttend(cursor.isNull(offset + 12) ? null : cursor.getShort(offset + 12) != 0);
        entity.setIsViewed(cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0);
        entity.setType(cursor.getInt(offset + 14));
        entity.setNum(cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15));
        entity.setMerchantId(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(ActivityInfo entity, long rowId) {
        return entity.getActivityId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(ActivityInfo entity) {
        if(entity != null) {
            return entity.getActivityId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "activityInfoList" to-many relationship of MerchantInfo. */
    public List<ActivityInfo> _queryMerchantInfo_ActivityInfoList(String merchantId) {
        synchronized (this) {
            if (merchantInfo_ActivityInfoListQuery == null) {
                QueryBuilder<ActivityInfo> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.MerchantId.eq(null));
                merchantInfo_ActivityInfoListQuery = queryBuilder.build();
            }
        }
        Query<ActivityInfo> query = merchantInfo_ActivityInfoListQuery.forCurrentThread();
        query.setParameter(0, merchantId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getMerchantInfoDao().getAllColumns());
            builder.append(" FROM ACTIVITY_INFO T");
            builder.append(" LEFT JOIN MERCHANT_INFO T0 ON T.\"MERCHANT_ID\"=T0.\"MERCHANT_UUID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ActivityInfo loadCurrentDeep(Cursor cursor, boolean lock) {
        ActivityInfo entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        MerchantInfo merchantInfo = loadCurrentOther(daoSession.getMerchantInfoDao(), cursor, offset);
        entity.setMerchantInfo(merchantInfo);

        return entity;    
    }

    public ActivityInfo loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
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
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ActivityInfo> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ActivityInfo> list = new ArrayList<ActivityInfo>(count);
        
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
    
    protected List<ActivityInfo> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ActivityInfo> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
