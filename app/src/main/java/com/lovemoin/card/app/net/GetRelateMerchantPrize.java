package com.lovemoin.card.app.net;

import android.support.annotation.NonNull;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.dto.MerchantPrizeDto;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzt on 15-9-10.
 * 获取与活动关联的商户礼品
 */
public abstract class GetRelateMerchantPrize {
    public GetRelateMerchantPrize(@NonNull String activityId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ACTIVITY_ID, activityId);

        String url = Config.SERVER_URL + Config.ACTION_GET_RELATE_MERCHANT_PRIZE;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    MerchantPrizeDto merchantPrize = new MerchantPrizeDto(jsonObject);
                    GetRelateMerchantPrize.this.onSuccess(merchantPrize);
                } catch (JSONException e) {
                    e.printStackTrace();
                    GetRelateMerchantPrize.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                GetRelateMerchantPrize.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(MerchantPrizeDto merchantPrize);

    public abstract void onFail(String message);
}
