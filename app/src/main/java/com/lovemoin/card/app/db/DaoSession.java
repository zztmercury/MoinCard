package com.lovemoin.card.app.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cardInfoDaoConfig;
    private final DaoConfig merchantInfoDaoConfig;
    private final DaoConfig imageInfoDaoConfig;
    private final DaoConfig storeInfoDaoConfig;
    private final DaoConfig activityInfoDaoConfig;
    private final DaoConfig giftPackInfoDaoConfig;
    private final DaoConfig ignoredAdInfoDaoConfig;

    private final CardInfoDao cardInfoDao;
    private final MerchantInfoDao merchantInfoDao;
    private final ImageInfoDao imageInfoDao;
    private final StoreInfoDao storeInfoDao;
    private final ActivityInfoDao activityInfoDao;
    private final GiftPackInfoDao giftPackInfoDao;
    private final IgnoredAdInfoDao ignoredAdInfoDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cardInfoDaoConfig = daoConfigMap.get(CardInfoDao.class).clone();
        cardInfoDaoConfig.initIdentityScope(type);

        merchantInfoDaoConfig = daoConfigMap.get(MerchantInfoDao.class).clone();
        merchantInfoDaoConfig.initIdentityScope(type);

        imageInfoDaoConfig = daoConfigMap.get(ImageInfoDao.class).clone();
        imageInfoDaoConfig.initIdentityScope(type);

        storeInfoDaoConfig = daoConfigMap.get(StoreInfoDao.class).clone();
        storeInfoDaoConfig.initIdentityScope(type);

        activityInfoDaoConfig = daoConfigMap.get(ActivityInfoDao.class).clone();
        activityInfoDaoConfig.initIdentityScope(type);

        giftPackInfoDaoConfig = daoConfigMap.get(GiftPackInfoDao.class).clone();
        giftPackInfoDaoConfig.initIdentityScope(type);

        ignoredAdInfoDaoConfig = daoConfigMap.get(IgnoredAdInfoDao.class).clone();
        ignoredAdInfoDaoConfig.initIdentityScope(type);

        cardInfoDao = new CardInfoDao(cardInfoDaoConfig, this);
        merchantInfoDao = new MerchantInfoDao(merchantInfoDaoConfig, this);
        imageInfoDao = new ImageInfoDao(imageInfoDaoConfig, this);
        storeInfoDao = new StoreInfoDao(storeInfoDaoConfig, this);
        activityInfoDao = new ActivityInfoDao(activityInfoDaoConfig, this);
        giftPackInfoDao = new GiftPackInfoDao(giftPackInfoDaoConfig, this);
        ignoredAdInfoDao = new IgnoredAdInfoDao(ignoredAdInfoDaoConfig, this);

        registerDao(CardInfo.class, cardInfoDao);
        registerDao(MerchantInfo.class, merchantInfoDao);
        registerDao(ImageInfo.class, imageInfoDao);
        registerDao(StoreInfo.class, storeInfoDao);
        registerDao(ActivityInfo.class, activityInfoDao);
        registerDao(GiftPackInfo.class, giftPackInfoDao);
        registerDao(IgnoredAdInfo.class, ignoredAdInfoDao);
    }
    
    public void clear() {
        cardInfoDaoConfig.getIdentityScope().clear();
        merchantInfoDaoConfig.getIdentityScope().clear();
        imageInfoDaoConfig.getIdentityScope().clear();
        storeInfoDaoConfig.getIdentityScope().clear();
        activityInfoDaoConfig.getIdentityScope().clear();
        giftPackInfoDaoConfig.getIdentityScope().clear();
        ignoredAdInfoDaoConfig.getIdentityScope().clear();
    }

    public CardInfoDao getCardInfoDao() {
        return cardInfoDao;
    }

    public MerchantInfoDao getMerchantInfoDao() {
        return merchantInfoDao;
    }

    public ImageInfoDao getImageInfoDao() {
        return imageInfoDao;
    }

    public StoreInfoDao getStoreInfoDao() {
        return storeInfoDao;
    }

    public ActivityInfoDao getActivityInfoDao() {
        return activityInfoDao;
    }

    public GiftPackInfoDao getGiftPackInfoDao() {
        return giftPackInfoDao;
    }

    public IgnoredAdInfoDao getIgnoredAdInfoDao() {
        return ignoredAdInfoDao;
    }

}
