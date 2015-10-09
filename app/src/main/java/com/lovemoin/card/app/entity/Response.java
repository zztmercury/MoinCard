package com.lovemoin.card.app.entity;

import com.lovemoin.card.app.constant.Config;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzt on 15-8-24.
 */
public class Response {
    private boolean success;
    private String message;
    private String param;

    public Response() {
    }

    public Response(String str) {
        try {
            JSONObject object = new JSONObject(str);
            success = object.getBoolean(Config.KEY_SUCCESS);
            message = object.getString(Config.KEY_MESSAGE);
            param = object.getString(Config.KEY_PARAM);
        } catch (JSONException e) {
            success = false;
            message = "失败: " + str;
            param = null;
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.put(Config.KEY_SUCCESS, success);
            object.put(Config.KEY_MESSAGE, message);
            object.put(Config.KEY_PARAM, param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
