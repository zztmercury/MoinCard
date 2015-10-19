package com.lovemoin.card.app.net;

import android.support.annotation.NonNull;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.dto.CouponDto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-9-10.
 * 获取与活动关联的商户礼品
 */
public abstract class GetRelateMerchantCoupon {
    public GetRelateMerchantCoupon(@NonNull String activityId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ACTIVITY_ID, activityId);

        String url = Config.SERVER_URL + Config.ACTION_GET_RELATE_MERCHANT_COUPON;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                List<CouponDto> prizeList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        prizeList.add(new CouponDto(array.getJSONObject(i)));
                    }
                    GetRelateMerchantCoupon.this.onSuccess(prizeList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    GetRelateMerchantCoupon.this.onFail("解析错误：" + e.getMessage());
                }
            }

            @Override
            public void onFail(String message) {
                GetRelateMerchantCoupon.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<CouponDto> prizeList);

    public abstract void onFail(String message);
}
