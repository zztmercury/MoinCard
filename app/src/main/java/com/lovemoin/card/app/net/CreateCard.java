package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-15.
 */
public abstract class CreateCard {
    public CreateCard(String cardUserId, String merchantId, String cardType) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_CARD_USER_ID, cardUserId);
        paramsMap.put(Config.KEY_MERCHANT_ID, merchantId);
        paramsMap.put(Config.KEY_CARD_TYPE, cardType);

        String url = Config.SERVER_URL + Config.ACTION_CREATE_CARD;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    CardInfo cardInfo = new CardInfo(array.getJSONObject(0));
                    CreateCard.this.onSuccess(cardInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    CreateCard.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                CreateCard.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(CardInfo cardInfo);

    public abstract void onFail(String message);
}
