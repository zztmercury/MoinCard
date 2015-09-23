package com.lovemoin.card.app.entity;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-9-21.
 */
public class ActivityType1 extends ActivityInfo {
    private List<String> merchantList = new ArrayList<>();

    private List<String> merchantIdList = new ArrayList<>();

    public ActivityType1(ActivityInfo activityInfo) {
        setActivityId(activityInfo.getActivityId());
        setBrief(activityInfo.getBrief());
        setBriefImg(activityInfo.getBriefImg());
        setName(activityInfo.getName());
        setType(activityInfo.getType());
        setStartDate(activityInfo.getStartDate());
        setEndDate(activityInfo.getEndDate());
        setIsOfficial(activityInfo.getIsOfficial());
        setIsAttend(activityInfo.getIsAttend());
        setIsTop(activityInfo.getIsTop());
    }

    public void completeFromJSON(JSONObject object) throws JSONException {
        setAddress(object.getString(Config.KEY_ACTIVITY_ADDR));
        JSONArray array = object.getJSONArray(Config.KEY_ACTIVITY_MERCHANT);
        for (int i = 0; i < array.length(); i++) {
            merchantList.add(array.getJSONObject(i).getString(Config.KEY_MERCHANT_NAME));
            merchantIdList.add(array.getJSONObject(i).getString(Config.KEY_MERCHANT_ID));
        }
        setDetail(object.getString(Config.KEY_ACTIVITY_DETAIL));
        setImg(object.getString(Config.KEY_ACTIVITY_DETAIL_IMAGE));
    }


    public List<String> getMerchantIdList() {
        return merchantIdList;
    }

    public List<String> getMerchantList() {
        return merchantList;
    }
}
