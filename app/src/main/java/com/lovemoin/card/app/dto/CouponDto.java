package com.lovemoin.card.app.dto;

import com.lovemoin.card.app.constant.Config;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzt on 15-9-10.
 */
public class CouponDto {
    private String couponId;
    private String couponName;
    private String merchantId;

    public CouponDto(JSONObject jsonObject) throws JSONException {
        couponId = jsonObject.getString(Config.KEY_POINT_CARD_ID);
        couponName = jsonObject.getString(Config.KEY_COUPON_NAME);
        merchantId = jsonObject.getString(Config.KEY_MERCHANT_UUID);
    }

    public String getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public String getMerchantId() {
        return merchantId;
    }
}
