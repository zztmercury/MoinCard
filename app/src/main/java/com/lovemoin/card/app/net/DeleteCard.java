package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-21.
 */
public abstract class DeleteCard {
    public DeleteCard(String cardCode) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_CARD_NUMBER, cardCode);

        String url = Config.SERVER_URL + Config.ACTION_DELETE_CARD;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                DeleteCard.this.onSuccess();
            }

            @Override
            public void onFail(String message) {
                DeleteCard.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess();

    public abstract void onFail(String message);
}
