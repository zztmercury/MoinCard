package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-10-14.
 */
public abstract class HasNum1Act {
    public HasNum1Act(String userId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_ID, userId);

        String url = Config.SERVER_URL + Config.ACTIOB_HAS_NUM_1_ACT;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                HasNum1Act.this.onSuccess(result);
            }

            @Override
            public void onFail(String message) {
                HasNum1Act.this.onFail(message);
            }
        };
    }

    protected abstract void onSuccess(String activityId);

    protected abstract void onFail(String message);
}
