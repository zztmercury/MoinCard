package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.GiftPackInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-10-13.
 */
public abstract class LoadGiftPackList {
    public LoadGiftPackList(String userId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_ID, userId);

        String url = Config.SERVER_URL + Config.ACTION_LOAD_GIFT_PACK_LIST;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    List<GiftPackInfo> giftPackList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        giftPackList.add(new GiftPackInfo(array.getJSONObject(i)));
                    }
                    LoadGiftPackList.this.onSuccess(giftPackList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadGiftPackList.this.onFail("解析失败：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                LoadGiftPackList.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<GiftPackInfo> giftPackList);

    public abstract void onFail(String message);
}
