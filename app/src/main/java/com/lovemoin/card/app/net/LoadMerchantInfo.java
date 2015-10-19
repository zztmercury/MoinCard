package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.MerchantInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-9.
 */
public abstract class LoadMerchantInfo {
    public LoadMerchantInfo(String cardCode) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_CARD_CODE, cardCode);

        String url = Config.SERVER_URL + Config.ACTION_LOAD_MERCHANT_INFO;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    MerchantInfo merchantInfo = new MerchantInfo(jsonObject);
                    LoadMerchantInfo.this.onSuccess(merchantInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadMerchantInfo.this.onFail("解析失败：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                LoadMerchantInfo.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(MerchantInfo merchantInfo);

    public abstract void onFail(String message);
}
