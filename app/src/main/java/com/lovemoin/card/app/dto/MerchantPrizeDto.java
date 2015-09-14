package com.lovemoin.card.app.dto;

import com.lovemoin.card.app.constant.Config;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzt on 15-9-10.
 */
public class MerchantPrizeDto {
    private String merchantId;
    private String merchantName;
    private String pointCardId;

    public MerchantPrizeDto() {
    }

    public MerchantPrizeDto(JSONObject jsonObject) throws JSONException {
        merchantId = jsonObject.getString(Config.KEY_MERCHANT_ID);
        merchantName = jsonObject.getString(Config.KEY_MERCHANT);
        pointCardId = jsonObject.getString(Config.KEY_POINT_CARD_ID);
    }


}
