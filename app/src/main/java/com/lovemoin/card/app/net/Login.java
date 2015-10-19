package com.lovemoin.card.app.net;

import android.os.Build;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.utils.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-8-24.
 */
public abstract class Login {
    /**
     * 登陆
     *
     * @param userTel  用户手机号
     * @param password 用户密码
     */
    public Login(String userTel, String password, String versionName) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(Config.KEY_USER_TEL, userTel);
        paramMap.put(Config.KEY_USER_PASSWORD, CommonUtil.MD5(password));
//        paramMap.put(Config.KEY_OS_VERSION, "Android " + Build.VERSION.RELEASE);
        paramMap.put(Config.KEY_OS_VERSION, versionName);
        paramMap.put(Config.KEY_MODEL, Build.MODEL);

        String url = Config.SERVER_URL + Config.ACTION_LOGIN;

        new NetConnection(url, paramMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Login.this.onSuccess(object.getString(Config.KEY_USER_TEL), object.getString(Config.KEY_ID));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Login.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                Login.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String userTel, String userId);

    public abstract void onFail(String message);
}
