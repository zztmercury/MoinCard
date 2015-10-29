package com.lovemoin.card.app.net;

import android.os.Build;
import android.support.annotation.Nullable;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.utils.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-8-31.
 */
public abstract class Register {
    /**
     * 注册及补全帐号密码
     *
     * @param userTel 用户手机号
     * @param userPwd 用户密码
     * @param userId 补全信息时需要
     */
    public Register(String userTel, String userPwd, String checkCode, String versionName, @Nullable final String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(Config.KEY_USER_TEL, userTel);
        paramMap.put(Config.KEY_USER_PASSWORD, CommonUtil.MD5(userPwd));
        paramMap.put(Config.KEY_CHECK_CODE, checkCode);
//        paramMap.put(Config.KEY_OS_VERSION, "Android " + Build.VERSION.RELEASE);
        paramMap.put(Config.KEY_OS_VERSION, versionName);
        paramMap.put(Config.KEY_MODEL, Build.MODEL);
        if (userId != null)
            paramMap.put(Config.KEY_USER_ID, userId);

        String url = Config.SERVER_URL + Config.ACTION_REGISTER;

        new NetConnection(url, paramMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    // 补全和登录时userId参数名不同
                    if (userId != null) {
                        Register.this.onSuccess(object.getString(Config.KEY_USER_TEL), object.getString(Config.KEY_USER_ID));
                    } else {
                        Register.this.onSuccess(object.getString(Config.KEY_USER_TEL), object.getString(Config.KEY_ID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Register.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                Register.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(String userTel, String id);

    public abstract void onFail(String message);
}
