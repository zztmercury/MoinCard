package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-24.
 */
public abstract class CheckVersion {
    public CheckVersion(int version) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_VERSION, String.valueOf(version));

        String url = Config.SERVER_URL + Config.ACTION_CHECK_VERSION;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                CheckVersion.this.onSuccess(result);
            }

            @Override
            public void onFail(String message) {
                CheckVersion.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String apkName);

    public abstract void onFail(String message);
}