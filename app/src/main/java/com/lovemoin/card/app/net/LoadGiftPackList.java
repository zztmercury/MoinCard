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
                JSONArray array;
                List<GiftPackInfo> giftPackList = new ArrayList<>();
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadGiftPackList.this.onFail("解析失败：" + e.getMessage());
                    return;
                }

                for (int i = 0; i < array.length(); i++) {
                    try {
                        giftPackList.add(new GiftPackInfo(array.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LoadGiftPackList.this.onSuccess(giftPackList);
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
