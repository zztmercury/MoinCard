package com.lovemoin.card.app.entity;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzt on 15-9-14.
 */
public class CardRecord {
    private String dateStr;
    private String operateStr;

    public CardRecord() {

    }

    public CardRecord(JSONObject jsonObject) throws JSONException {
        dateStr = jsonObject.getString(Config.KEY_OPERATE_DATE);
        StringBuffer buffer = new StringBuffer();
        String operateType;
        int point;
        buffer.append(operateType = jsonObject.getString(Config.KEY_OPERATE_TYPE));
        if ((point = jsonObject.getInt(Config.KEY_OPERATE_POINT)) == 0 || operateType.contains("领取新卡")
                || operateType.contains("增加积点") || operateType.contains("赠送积点")
                || operateType.contains("获得") || operateType.contains("签到"))
            buffer.append("+");
        else
            buffer.append("-");
        buffer.append(point);
        operateStr = buffer.toString();
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getOperateStr() {
        return operateStr;
    }

    public void setOperateStr(String operateStr) {
        this.operateStr = operateStr;
    }
}
