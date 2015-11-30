package com.lovemoin.card.app.net;

import android.os.Build;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-11-16.
 */
public abstract class LoginByWeChat {
    public LoginByWeChat(String code, String versionName, String deviceId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_CODE, code);
        paramsMap.put(Config.KEY_OS_VERSION, versionName);
        paramsMap.put(Config.KEY_DEVICE_ID, deviceId);
        paramsMap.put(Config.KEY_MODEL, Build.MODEL);

        String url = Config.SERVER_URL + Config.ACTION_LOGIN_BY_WE_CHAT;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoginByWeChat.this.onFail("解析失败：" + e.getMessage());
                    return;
                }
                try {
                    LoginByWeChat.this.onSuccess(jsonObject.getString(Config.KEY_USER_ID),
                            jsonObject.getString(Config.KEY_USER_TEL),
                            jsonObject.getString(Config.KEY_USER_IMG),
                            jsonObject.getString(Config.KEY_USERNAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                LoginByWeChat.this.onFail(message);
            }
        };
    }

    protected abstract void onSuccess(String userId, String userTel, String userImg, String username);

    protected abstract void onFail(String message);
}
