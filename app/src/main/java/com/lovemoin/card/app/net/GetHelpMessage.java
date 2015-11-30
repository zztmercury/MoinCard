package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-11-18.
 */
public abstract class GetHelpMessage {
    public GetHelpMessage() {
        Map<String, String> paramsMap = new HashMap<>();

        String url = Config.SERVER_URL + Config.ACTION_GET_HELP;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                GetHelpMessage.this.onSuccess(result);
            }

            @Override
            public void onFail(String message) {
                GetHelpMessage.this.onFail(message);
            }
        };
    }

    protected abstract void onSuccess(String helpMessage);

    protected abstract void onFail(String message);
}
