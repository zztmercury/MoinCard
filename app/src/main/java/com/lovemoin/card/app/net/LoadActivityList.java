package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-8-31.<br/>
 * 向服务器查询活动列表，并通过回调方法处理查询到的结果
 *
 * @author zzt
 */
public abstract class LoadActivityList {
    public static final int TYPE_RELATED = 1;
    public static final int TYPE_ALL = 2;
    private Map<String, String> paramsMap = new HashMap<>();

    /**
     * 获取活动列表
     *
     * @param viewType       请求的活动分类<br/>
     *                       TYPE_RELATED:与用户相关的全部活动<br/>
     *                       TYPE_ALL:全部活动
     * @param userId         用户ID
     * @param lastSearchTime 最后一次查询时间
     */
    public LoadActivityList(int viewType, String userId, long lastSearchTime) {
        paramsMap.put(Config.KEY_VIEW_TYPE, String.valueOf(viewType));
        paramsMap.put(Config.KEY_USER_ID, userId);
        paramsMap.put(Config.KEY_LAST_SEARCH_TIME, String.valueOf(lastSearchTime / 1000));

        String url = Config.SERVER_URL + Config.ACTION_LOAD_ACTIVITY_LIST;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    List<ActivityInfo> activityInfoList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            activityInfoList.add(new ActivityInfo(array.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    LoadActivityList.this.onSuccess(activityInfoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadActivityList.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                LoadActivityList.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<ActivityInfo> activityInfoList);

    public abstract void onFail(String message);
}
