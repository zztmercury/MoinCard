package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-21.
 */
public abstract class SendCode {
    public SendCode(String userTel) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_TEL, userTel);

        String url = Config.SERVER_URL + Config.ACTION_SEND_CODE;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                SendCode.this.onSuccess(result);
            }

            @Override
            public void onFail(String message) {
                SendCode.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String checkCode);

    public abstract void onFail(String message);
}
