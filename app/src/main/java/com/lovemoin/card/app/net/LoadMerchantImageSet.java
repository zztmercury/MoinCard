package com.lovemoin.card.app.net;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ImageInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-9-14.
 */
public abstract class LoadMerchantImageSet {
    public LoadMerchantImageSet(String merchantId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Config.KEY_ID, merchantId);

        String url = Config.SERVER_URL + Config.ACTION_LOAD_MERCHANT_IMAGE_SET;

        new NetConnection(url, paramsMap) {
            @Override
            public void onSuccess(String result) {
                List<ImageInfo> imageList = new ArrayList<>();
                JSONArray array;
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadMerchantImageSet.this.onFail("解析失败：" + e.getMessage());
                    return;
                }

                for (int i = 0; i < array.length(); i++) {
                    try {
                        imageList.add(new ImageInfo(array.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LoadMerchantImageSet.this.onSuccess(imageList);
            }

            @Override
            public void onFail(String message) {
                LoadMerchantImageSet.this.onFail(message);
            }
        };
    }

    public abstract void onSuccess(List<ImageInfo> imageList);

    public abstract void onFail(String message);
}
