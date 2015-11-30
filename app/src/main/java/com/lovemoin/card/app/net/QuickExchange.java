package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

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
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String activityId = null;
                String shareUrl = null;
                String title = null;
                if (object != null) {
                    try {
                        activityId = object.getString(Config.KEY_ACTIVITY_ID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        shareUrl = object.getString(Config.KEY_SHARE_URL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        title = object.getString(Config.KEY_TITLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                QuickExchange.this.onSuccess(activityId, shareUrl, title);
            }

            @Override
            public void onFail(String message) {
                QuickExchange.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String activityId, String shareUrl, String title);

    public abstract void onFail(String message);
}
