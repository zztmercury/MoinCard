package com.lovemoin.card.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 8): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 8;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        CardInfoDao.createTable(db, ifNotExists);
        MerchantInfoDao.createTable(db, ifNotExists);
        ImageInfoDao.createTable(db, ifNotExists);
        StoreInfoDao.createTable(db, ifNotExists);
        ActivityInfoDao.createTable(db, ifNotExists);
        GiftPackInfoDao.createTable(db, ifNotExists);
        IgnoredAdInfoDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        CardInfoDao.dropTable(db, ifExists);
        MerchantInfoDao.dropTable(db, ifExists);
        ImageInfoDao.dropTable(db, ifExists);
        StoreInfoDao.dropTable(db, ifExists);
        ActivityInfoDao.dropTable(db, ifExists);
        GiftPackInfoDao.dropTable(db, ifExists);
        IgnoredAdInfoDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(CardInfoDao.class);
        registerDaoClass(MerchantInfoDao.class);
        registerDaoClass(ImageInfoDao.class);
        registerDaoClass(StoreInfoDao.class);
        registerDaoClass(ActivityInfoDao.class);
        registerDaoClass(GiftPackInfoDao.class);
        registerDaoClass(IgnoredAdInfoDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
