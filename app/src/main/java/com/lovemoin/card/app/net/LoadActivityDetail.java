package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.entity.ActivityType1;
import com.lovemoin.card.app.entity.ActivityType3;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-2.
 */
public abstract class LoadActivityDetail {
    /**
     * 获取活动详情
     *
     * @param activityInfo 活动
     * @param userId       用户Id
     */
    public LoadActivityDetail(final ActivityInfo activityInfo, String userId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ACTIVITY_ID, activityInfo.getActivityId());
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_ACTIVITY_TYPE, String.valueOf(activityInfo.getType()));

        String url = Config.SERVER_URL + Config.ACTION_LOAD_ACTIVITY_DETAIL;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    switch (activityInfo.getType()) {
                        case 1:
                            ActivityType1 completeInfo1 = new ActivityType1(activityInfo);
                            completeInfo1.completeFromJSON(jsonObject);
                            LoadActivityDetail.this.onSuccess(completeInfo1);
                            break;
                        case 2:
                            break;
                        case 3:
                            ActivityType3 completeInfo3 = new ActivityType3(activityInfo);
                            completeInfo3.completeFromJSON(jsonObject);
                            LoadActivityDetail.this.onSuccess(completeInfo3);
                            break;
                        default:
                            LoadActivityDetail.this.onFail("不支持该活动种类");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadActivityDetail.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                LoadActivityDetail.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(ActivityInfo completeActivityInfo);

    public abstract void onFail(String message);
}
