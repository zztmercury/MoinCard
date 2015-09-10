package com.lovemoin.card.app;

import android.app.Application;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by zzt on 15-8-24.
 */
public class MoinCardApplication extends Application {
    public static final String APP_NAME = "moinCard";
    public static final String KEY_LOGIN_STATUS = "loginStatus";

    private CardInfo currentCard;

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(configuration);
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

    public boolean isLogin() {
        return getSharedPreferences(APP_NAME, MODE_PRIVATE).getBoolean(KEY_LOGIN_STATUS, false);
    }

    public void cacheLoginStatus(boolean loginStatus) {
        getSharedPreferences(APP_NAME, MODE_PRIVATE).edit().putBoolean(KEY_LOGIN_STATUS, loginStatus).commit();
    }

    public CardInfo getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(CardInfo currentCard) {
        this.currentCard = currentCard;
    }
}
