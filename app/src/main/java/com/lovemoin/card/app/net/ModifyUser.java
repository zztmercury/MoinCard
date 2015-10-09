package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-23.
 */
public abstract class ModifyUser {
    public ModifyUser(String userTel, String newPassword, String oldPassword) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_USER_TEL, userTel);
        paramsMap.put(Config.KEY_NEW_PASSWORD, CommonUtil.MD5(newPassword));
        paramsMap.put(Config.KEY_OLD_PASSWORD, CommonUtil.MD5(oldPassword));

        String url = Config.SERVER_URL + Config.ACTION_MODIFY_USER;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                ModifyUser.this.onSuccess();
            }

            @Override
            public void onFail(String message) {
                ModifyUser.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess();

    public abstract void onFail(String message);
}
