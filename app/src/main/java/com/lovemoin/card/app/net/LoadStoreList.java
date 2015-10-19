package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.StoreInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-9-14.
 */
public abstract class LoadStoreList {
    public LoadStoreList(String merchantId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ID, merchantId);

        String url = Config.SERVER_URL + Config.ACTION_LOAD_STORE_LIST;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                List<StoreInfo> storeList = new ArrayList<>();
                if (!result.equals("null")) {
                    try {
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            storeList.add(new StoreInfo(array.getJSONObject(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LoadStoreList.this.onFail("解析错误：" + e.getMessage());
                        return;
                    }
                }
                LoadStoreList.this.onSuccess(storeList);
            }

            @Override
            public void onFail(String message) {
                LoadStoreList.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<StoreInfo> storeList);

    public abstract void onFail(String message);
}
