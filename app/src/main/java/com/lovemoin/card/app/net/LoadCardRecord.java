package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.entity.CardRecord;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-9-14.
 */
public abstract class LoadCardRecord {
    public LoadCardRecord(String userTel, String cardNumber) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_TEL, userTel);
        paramsMap.put(Config.KEY_CARD_NUMBER, cardNumber.substring(16));

        String url = Config.SERVER_URL + Config.ACTION_LOAD_CARD_RECORD;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                List<CardRecord> recordList = new ArrayList<>();
                if (!result.equals("null")) {
                    try {
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            recordList.add(new CardRecord(array.getJSONObject(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LoadCardRecord.this.onFail("解析失败：" + e.getMessage());
                    }
                }
                LoadCardRecord.this.onSuccess(recordList);
            }

            @Override
            public void onFail(String message) {
                LoadCardRecord.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<CardRecord> recordList);

    public abstract void onFail(String message);
}
