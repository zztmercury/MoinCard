package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-10-13.
 */
public abstract class GetGiftForNewUserByCode {
    public GetGiftForNewUserByCode(String code) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_GIFT_CODE, code);

        String url = Config.SERVER_URL + Config.ACTION_GET_GIFT_FOR_NEW_USER_BY_CODE;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    GetGiftForNewUserByCode.this.onSuccess(object.getString(Config.KEY_POINT_CARD_ID));
                } catch (JSONException e) {
                    e.printStackTrace();
                    GetGiftForNewUserByCode.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                GetGiftForNewUserByCode.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String couponId);

    public abstract void onFail(String message);
}
