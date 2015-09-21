package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-21.
 */
public abstract class LoadActivityByMerchant {
    public LoadActivityByMerchant(String userId, long lastSearchTime, String merchantUUID) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_LAST_SEARCH_TIME, String.valueOf(lastSearchTime));
        paramsMap.put(Config.KEY_MERCHANT_UUID, merchantUUID);

        String url = Config.SERVER_URL + Config.ACTION_LOAD_ACTIVITY_LIST_BY_MERCHNAT;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(String message) {

            }
        };
    }

    public abstract void onSuccess(ActivityInfo activityInfo);

    public abstract void onFail(String message);
}
