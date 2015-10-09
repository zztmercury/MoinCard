package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-17.
 */
public abstract class QuickExchange {
    public QuickExchange(String checkValue, String userId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_CHECK_VALUE, checkValue);
        paramsMap.put(Config.KEY_USER_ID, userId);

        String url = Config.SERVER_URL + Config.ACTION_QUICK_EXCHANGE;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                QuickExchange.this.onSuccess();
            }

            @Override
            public void onFail(String message) {
                QuickExchange.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess();

    public abstract void onFail(String message);
}
