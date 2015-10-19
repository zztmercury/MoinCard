package com.lovemoin.card.app.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-10-13.
 */
public abstract class GetGift {
    public GetGift(@NonNull String userId, @NonNull String giftId, @Nullable String code) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_GIFT_ID, giftId);
        if (!TextUtils.isEmpty(code))
            paramsMap.put(Config.KEY_GIFT_CODE, code);

        String url = Config.SERVER_URL + Config.ACTION_GET_GIFT;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String giftName = object.getString(Config.KEY_GIFT_NAME);
                    String giftImg = object.getString(Config.KEY_GIFT_IMG);
                    GetGift.this.onSuccess(giftName, giftImg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    GetGift.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                GetGift.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String giftName, String giftImg);

    public abstract void onFail(String message);
}
