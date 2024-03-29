package com.lovemoin.card.app.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.lovemoin.card.app.db.GiftPackInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GIFT_PACK_INFO".
*/
public class GiftPackInfoDao extends AbstractDao<GiftPackInfo, String> {

    public static final String TABLENAME = "GIFT_PACK_INFO";

    /**
     * Properties of entity GiftPackInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property GiftId = new Property(0, String.class, "giftId", true, "GIFT_ID");
        public final static Property Priority = new Property(1, Integer.class, "priority", false, "PRIORITY");
        public final static Property Comment = new Property(2, String.class, "comment", false, "COMMENT");
        public final static Property Ignore = new Property(3, Boolean.class, "ignore", false, "IGNORE");
        public final static Property NeedCode = new Property(4, Boolean.class, "needCode", false, "NEED_CODE");
    };


    public GiftPackInfoDao(DaoConfig config) {
        super(config);
    }
    
    public GiftPackInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GIFT_PACK_INFO\" (" + //
                "\"GIFT_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: giftId
                "\"PRIORITY\" INTEGER," + // 1: priority
                "\"COMMENT\" TEXT," + // 2: comment
                "\"IGNORE\" INTEGER," + // 3: ignore
                "\"NEED_CODE\" INTEGER);"); // 4: needCode
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GIFT_PACK_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GiftPackInfo entity) {
        stmt.clearBindings();
 
        String giftId = entity.getGiftId();
        if (giftId != null) {
            stmt.bindString(1, giftId);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(2, priority);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(3, comment);
        }
 
        Boolean ignore = entity.getIgnore();
        if (ignore != null) {
            stmt.bindLong(4, ignore ? 1L: 0L);
        }
 
        Boolean needCode = entity.getNeedCode();
        if (needCode != null) {
            stmt.bindLong(5, needCode ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GiftPackInfo readEntity(Cursor cursor, int offset) {
        GiftPackInfo entity = new GiftPackInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // giftId
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // priority
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // comment
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // ignore
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0 // needCode
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GiftPackInfo entity, int offset) {
        entity.setGiftId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPriority(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setComment(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIgnore(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setNeedCode(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(GiftPackInfo entity, long rowId) {
        return entity.getGiftId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(GiftPackInfo entity) {
        if(entity != null) {
            return entity.getGiftId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
