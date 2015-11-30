package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-11-26.
 */
public abstract class SendShareLog {
    public SendShareLog(String userId, String activityId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_ACTIVITY_ID, activityId);

        String url = Config.SERVER_URL + Config.ACTION_SEND_SHARE_LOG;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                onFinish();
            }

            @Override
            public void onFail(String message) {
                onFinish();
            }
        };
    }

    protected abstract void onFinish();
}
