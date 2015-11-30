package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-8-25.
 */
public abstract class LoadCardList {
    /**
     * 获取卡片列表
     *
     * @param userId 用户Id
     */
    public LoadCardList(String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(Config.KEY_USER_ID, userId);
        String url = Config.SERVER_URL + Config.ACTION_LOAD_CARD_LIST;
        new NetConnection(url, paramMap) {
            @Override
            public void onSuccess(String result) {
                JSONArray array;
                List<CardInfo> cardInfoList = new ArrayList<>();
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadCardList.this.onFail("解析错误：" + e.getMessage());
                    return;
                }
                for (int i = 0; i < array.length(); i++) {
                    try {
                        cardInfoList.add(new CardInfo(array.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LoadCardList.this.onSuccess(cardInfoList);

            }

            @Override
            public void onFail(String message) {
                LoadCardList.this.onFail(message);
            }
        };
    }

    protected abstract void onSuccess(List<CardInfo> cardInfoList);

    protected abstract void onFail(String message);
}
