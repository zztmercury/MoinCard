package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.entity.AdInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-11-23.
 */
public abstract class LoadAdList {
    public LoadAdList(String userId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_ID, userId);

        String url = Config.SERVER_URL + Config.ACTIOB_LOAD_AD_LIST;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                List<AdInfo> adList = new ArrayList<>();
                JSONArray array;
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onFail("解析错误：" + e.getMessage());
                    return;
                }

                for (int i = 0; i < array.length(); i++) {
                    try {
                        adList.add(new AdInfo(array.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LoadAdList.this.onSuccess(adList);
            }

            @Override
            public void onFail(String message) {
                LoadAdList.this.onFail(message);
            }
        };
    }

    protected abstract void onSuccess(List<AdInfo> adList);

    protected abstract void onFail(String message);
}
