package com.lovemoin.card.app;

import android.app.Application;
import com.lovemoin.card.app.constant.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by zzt on 15-8-24.
 */
public class MoinCardApplication extends Application {
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
        return getSharedPreferences(Config.APP_NAME, MODE_PRIVATE).getString(Config.KEY_USER_ID, null);
    }

    public void cachedUserId(String userId) {
        getSharedPreferences(Config.APP_NAME, MODE_PRIVATE).edit().putString(Config.KEY_USER_ID, userId).commit();
    }

    public String getCachedUserTel() {
        return getSharedPreferences(Config.APP_NAME, MODE_PRIVATE).getString(Config.KEY_USER_TEL, null);
    }

    public void cacheUserTel(String userTel) {
        getSharedPreferences(Config.APP_NAME, MODE_PRIVATE).edit().putString(Config.KEY_USER_TEL, userTel).commit();
    }

    public boolean isLogin() {
        return getSharedPreferences(Config.APP_NAME, MODE_PRIVATE).getBoolean(Config.KEY_LOGIN_STATUS, false);
    }

    public void cacheLoginStatus(boolean loginStatus) {
        getSharedPreferences(Config.APP_NAME, MODE_PRIVATE).edit().putBoolean(Config.KEY_LOGIN_STATUS, loginStatus).commit();
    }
}
