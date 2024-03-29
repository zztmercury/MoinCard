package com.lovemoin.card.app;

import android.app.Application;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.ActivityInfoDao;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.DaoMaster;
import com.lovemoin.card.app.db.DaoSession;
import com.lovemoin.card.app.db.GiftPackInfoDao;
import com.lovemoin.card.app.db.IgnoredAdInfoDao;
import com.lovemoin.card.app.entity.DeviceInfo;
import com.lovemoin.card.app.net.LoadActivityList;
import com.lovemoin.card.app.net.LoadCardList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by zzt on 15-8-24.
 */
public class MoinCardApplication extends Application {
    public static final String APP_NAME = "moinCard";
    public static final String KEY_LOGIN_STATUS = "loginStatus";
    public static final String KEY_LAST_SEARCH_TIME = "lastSearchTime";
    public static final String KEY_SHOW_NEW_VERSION_ON_START = "showNewVersionOnStart";
    public static final String KEY_HAS_NEW_VERSION = "hasNewVersion";
    public static final String KEY_IS_FIRST_TIME_INSTALLED = "isFirstTimeInstalled";
    public static final String KEY_IS_USER_FIRST_TIME = "isUserFirstTime";
    public static final int MODE_NFC = 0;
    public static final int MODE_BLUETOOTH = 1;
    public int stat = DeviceInfo.OPER_WAITE;

    private CardInfo currentCard;
    private boolean isExchange;
    private int connectMode = MODE_BLUETOOTH;
    private boolean promptNfc = true;
    private CardInfoDao cardInfoDao;
    private ActivityInfoDao activityInfoDao;
    private GiftPackInfoDao giftPackInfoDao;
    private IgnoredAdInfoDao ignoredInfoDao;

    private String currentActivityId;
    private boolean showAd = true;

    public boolean isShowAd() {
        return showAd;
    }

    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(configuration);
        promptNfc = true;
        initDao();
    }

    private void initDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "moinCard.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        cardInfoDao = daoSession.getCardInfoDao();
        activityInfoDao = daoSession.getActivityInfoDao();
        giftPackInfoDao = daoSession.getGiftPackInfoDao();
        ignoredInfoDao = daoSession.getIgnoredAdInfoDao();
    }

    public String getCachedUserId() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getString(Config.KEY_USER_ID, null);
    }

    public void cachedUserId(String userId) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putString(Config.KEY_USER_ID, userId).commit();
    }

    public String getCachedUserTel() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getString(Config.KEY_USER_TEL, null);
    }

    public void cacheUserTel(String userTel) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putString(Config.KEY_USER_TEL, userTel).commit();
    }

    public String getCachedUserImg() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getString(Config.KEY_USER_IMG, null);
    }

    public void cacheUserImg(String userImg) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putString(Config.KEY_USER_IMG, userImg).commit();
    }

    public String getCachedUsername() {
        String username = getSharedPreferences(APP_NAME, MODE_PRIVATE).getString(Config.KEY_USERNAME, null);
        if (TextUtils.isEmpty(username))
            return getCachedUserTel();
        else return username;
    }

    public void cacheUsername(String username) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putString(Config.KEY_USERNAME, username).commit();
    }

    public boolean isLogin() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getBoolean(KEY_LOGIN_STATUS, true);
    }

    public void cacheLoginStatus(boolean loginStatus) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putBoolean(KEY_LOGIN_STATUS, loginStatus).commit();
    }

    public boolean isShowNewVersionOnStart() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getBoolean(KEY_SHOW_NEW_VERSION_ON_START, true);
    }

    public void cacheShowNewVersionOnStart(boolean isShowOnStart) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putBoolean(KEY_SHOW_NEW_VERSION_ON_START, isShowOnStart).commit();
    }

    public boolean hasNewVersion() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getBoolean(KEY_HAS_NEW_VERSION, false);
    }

    public void cacheHasNewVersion(boolean hasNewVersion) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putBoolean(KEY_HAS_NEW_VERSION, hasNewVersion).commit();
    }

    public void cacheLastSearchTime(long lastSearchTime) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putLong(KEY_LAST_SEARCH_TIME, lastSearchTime).commit();
    }

    public long getCachedLastSearchTime() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getLong(KEY_LAST_SEARCH_TIME, 0);
    }

    public CardInfo getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(CardInfo currentCard) {
        this.currentCard = currentCard;
    }

    public boolean isExchange() {
        return isExchange;
    }

    public void setIsExchange(boolean isExchange) {
        this.isExchange = isExchange;
    }

    public void reset() {
        currentCard = null;
        isExchange = false;
        cacheLoginStatus(false);
        cachedUserId(null);
        cacheUserTel(null);
        cacheUsername(null);
        cacheUserImg(null);
        cacheLastSearchTime(0);
        cardInfoDao.deleteAll();
        activityInfoDao.deleteAll();
        giftPackInfoDao.deleteAll();
        setUserFirstTime(false);
        MaterialShowcaseView.resetAll(getApplicationContext());
    }

    public CardInfoDao getCardInfoDao() {
        return cardInfoDao;
    }

    public ActivityInfoDao getActivityInfoDao() {
        return activityInfoDao;
    }

    public GiftPackInfoDao getGiftPackInfoDao() {
        return giftPackInfoDao;
    }

    public void updateCardInfoFromServer(final boolean showToast) {
        new LoadCardList(getCachedUserId()) {
            @Override
            protected void onSuccess(List<CardInfo> cardInfoList) {
                cardInfoDao.deleteAll();
                cardInfoDao.insertInTx(cardInfoList);
                if (showToast)
                    Toast.makeText(getApplicationContext(), R.string.update_point_card_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onFail(String message) {
                if (showToast)
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void updateActivityInfoFromServer(final boolean showToast) {
        new LoadActivityList(LoadActivityList.TYPE_RELATED, getCachedUserId(), 0) {
            @Override
            public void onSuccess(List<ActivityInfo> activityInfoList) {
                activityInfoDao.deleteAll();
                activityInfoDao.insertInTx(activityInfoList);
                if (showToast)
                    Toast.makeText(getApplicationContext(), R.string.update_point_card_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                if (showToast)
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFirstTimeInstalled() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getBoolean(KEY_IS_FIRST_TIME_INSTALLED, true);
    }

    public void setFirstTimeInstalled(boolean isFirstTimeInstalled) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putBoolean(KEY_IS_FIRST_TIME_INSTALLED, isFirstTimeInstalled).commit();
    }

    public boolean isUserFirstTime() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getBoolean(KEY_IS_USER_FIRST_TIME, true);
    }

    public void setUserFirstTime(boolean isUserFirstTime) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putBoolean(KEY_IS_USER_FIRST_TIME, isUserFirstTime).commit();
    }

    public int getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(int connectMode) {
        this.connectMode = connectMode;
    }

    public boolean isPromptNfc() {
        return promptNfc;
    }

    public void setPromptNfc(boolean promptNfc) {
        this.promptNfc = promptNfc;
    }

    public IgnoredAdInfoDao getIgnoredInfoDao() {
        return ignoredInfoDao;
    }

    public String getCurrentActivityId() {
        return currentActivityId;
    }

    public void setCurrentActivityId(String currentActivityId) {
        this.currentActivityId = currentActivityId;
    }
}
