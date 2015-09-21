package com.lovemoin.card.app.net;

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
     * 注册
     *
     * @param userTel   用户手机号
     * @param userPwd   用户密码
     * @param OSVersion 注册时手机操作系统版本
     * @param model     注册所用手机型号
     */
    public Register(String userTel, String userPwd, String OSVersion, String model) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(Config.KEY_USER_TEL, userTel);
        paramMap.put(Config.KEY_USER_PASSWORD, CommonUtil.MD5(userPwd));
        paramMap.put(Config.KEY_OS_VERSION, OSVersion);
        paramMap.put(Config.KEY_MODEL, model);

        String url = Config.SERVER_URL + Config.ACTION_REGISTER;

        new NetConnection(url, paramMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Register.this.onSuccess(object.getString(Config.KEY_USER_TEL), object.getString("id"));
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
