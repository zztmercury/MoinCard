package com.lovemoin.card.app.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-10.
 * 领取奖品
 *
 * @author zzt
 */
public abstract class GetPrize {
    public GetPrize(@NonNull String activityId, @NonNull String userId, int type, int num, @Nullable String merchantId, @Nullable String pointCardId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ACTIVITY_ID, activityId);
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_ACTIVITY_TYPE, String.valueOf(type));
        paramsMap.put(Config.KEY_ACTIVITY_NUM, String.valueOf(num));
        paramsMap.put(Config.KEY_MERCHANT_UUID, merchantId);
        paramsMap.put(Config.KEY_POINT_CARD_ID, pointCardId);

        String url = Config.SERVER_URL + Config.ACTION_GET_PRIZE;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String giftImg = jsonObject.getString(Config.KEY_GIFT_IMG);
                    String giftName = jsonObject.getString(Config.KEY_GIFT_NAME);
                    GetPrize.this.onSuccess(giftImg, giftName);
                } catch (JSONException e) {
                    e.printStackTrace();
                    GetPrize.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                GetPrize.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String giftImg, String giftName);

    public abstract void onFail(String message);
}
