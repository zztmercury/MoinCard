package com.lovemoin.card.app.entity;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by zzt on 15-11-23.
 */
public class AdInfo {
    private String activityId;
    private String message;
    private Date startTime;
    private Date endTime;

    public AdInfo(JSONObject object) {
        try {
            activityId = object.getString(Config.KEY_ACTIVITY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            message = object.getString(Config.KEY_MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            startTime = new Date(object.getLong(Config.KEY_START_TIME) * 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            endTime = new Date(object.getLong(Config.KEY_END_TIME) * 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getActivityId() {
        return activityId;
    }

    public String getMessage() {
        return message;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
