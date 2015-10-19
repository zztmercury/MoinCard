package com.lovemoin.card.app.entity;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-9-6.
 * 任务类活动，按步骤进行
 */
public class ActivityType3 extends ActivityInfo {
    private List<String> merchantList = new ArrayList<>();

    private List<String> merchantIdList = new ArrayList<>();
    private int currentStep;
    private int totalStep;
    private String stepText;
    private int memberCount;


    public ActivityType3(ActivityInfo activityInfo) throws JSONException {
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

    private void initFromJSON(JSONObject object) throws JSONException {
        setIsAttend(object.getInt(Config.KEY_IS_ATTEND) == 1);
        JSONArray array = object.getJSONArray(Config.KEY_ACTIVITY_MERCHANT);
        for (int i = 0; i < array.length(); i++) {
            merchantList.add(array.getJSONObject(i).getString(Config.KEY_MERCHANT_NAME));
            merchantIdList.add(array.getJSONObject(i).getString(Config.KEY_MERCHANT_ID));
        }
        setImg(object.getString(Config.KEY_ACTIVITY_DETAIL_IMAGE));
        setDetail(object.getString(Config.KEY_ACTIVITY_DETAIL));
        currentStep = object.getInt(Config.KEY_ACTIVITY_STEP);
        totalStep = object.getInt(Config.KEY_ACTIVITY_TOTAL_STEP);
        stepText = object.getString(Config.KEY_ACTIVITY_STEP_TEXT);
        memberCount = object.getInt(Config.KEY_ACTIVITY_MEMBER_COUNT);
        setNum(object.getInt(Config.KEY_ACTIVITY_NUM));
        setAddress(object.getString(Config.KEY_ACTIVITY_ADDR));
    }

    public void completeFromJSON(JSONObject object) throws JSONException {
        initFromJSON(object);
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public int getTotalStep() {
        return totalStep;
    }

    public void setTotalStep(int totalStep) {
        this.totalStep = totalStep;
    }

    public String getStepText() {
        return stepText;
    }

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public List<String> getMerchantIdList() {
        return merchantIdList;
    }

    public List<String> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<String> merchantList) {
        this.merchantList = merchantList;
    }
}


