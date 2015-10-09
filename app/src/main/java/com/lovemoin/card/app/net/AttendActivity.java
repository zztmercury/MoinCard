package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-2.
 */
public abstract class AttendActivity {
    /**
     * 参加活动
     *
     * @param activityId 活动Id
     * @param userId     用户Id
     * @param type       活动类型
     * @param num        活动详情编号
     */
    public AttendActivity(String activityId, String userId, String merchantId, int type, int num) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ACTIVITY_ID, activityId);
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_ACTIVITY_TYPE, String.valueOf(type));
        paramsMap.put(Config.KEY_ACTIVITY_NUM, String.valueOf(num));
        paramsMap.put(Config.KEY_MERCHANT_ID, merchantId);

        String url = Config.SERVER_URL + Config.ACTION_ATTEND_ACTIVITY;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                AttendActivity.this.onSuccess();
            }

            @Override
            public void onFail(String message) {
                AttendActivity.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess();

    public abstract void onFail(String message);
}
