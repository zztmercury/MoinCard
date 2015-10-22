package com.lovemoin.card.app.net;

import android.os.Build;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-10-20.
 */
public abstract class LoginByImei {
    public LoginByImei(String deviceId, String versionName) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_DEVICE_ID, deviceId);
        paramsMap.put(Config.KEY_OS_VERSION, versionName);
        paramsMap.put(Config.KEY_MODEL.replaceFirst("m", "M"), Build.MODEL);

        String url = Config.SERVER_URL + Config.ACTION_LOGIN_BY_IMEI;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                LoginByImei.this.onSuccess(result);
            }

            @Override
            public void onFail(String message) {
                LoginByImei.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String userId);

    public abstract void onFail(String message);
}
