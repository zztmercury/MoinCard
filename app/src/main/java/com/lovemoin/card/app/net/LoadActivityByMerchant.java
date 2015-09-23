package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                List<ActivityInfo> activityList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        activityList.add(new ActivityInfo(array.getJSONObject(i)));
                    }
                    LoadActivityByMerchant.this.onSuccess(activityList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadActivityByMerchant.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                LoadActivityByMerchant.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<ActivityInfo> activityList);

    public abstract void onFail(String message);
}
