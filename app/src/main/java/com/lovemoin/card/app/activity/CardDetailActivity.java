package com.lovemoin.card.app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.utils.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by zzt on 15-9-14.
 */
public class CardDetailActivity extends BaseActivity {
    private ImageView imgCardDetail;
    private TextView textCardDesc;
    private TextView textCardCode;
    private TextView textCardDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        initView();
        initData();
    }

    private void initView() {
        imgCardDetail = (ImageView) findViewById(R.id.imgCardDetail);
        textCardDesc = (TextView) findViewById(R.id.textCardDesc);
        textCardCode = (TextView) findViewById(R.id.textCardCode);
        textCardDuration = (TextView) findViewById(R.id.textCardDuration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        ImageLoader loader = ImageLoader.getInstance();
        CardInfo cardInfo = ((MoinCardApplication) getApplication()).getCurrentCard();
        loader.displayImage(Config.SERVER_URL + "/moinbox/" + cardInfo.getCardImg(), imgCardDetail, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ((ImageView) view).setImageResource(R.drawable.nocard);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        textCardCode.setText(cardInfo.getCardCode().substring(16));
        String datePattern = "yyyy-MM-dd";
        textCardDuration.setText(String.format("%s至%s", DateUtil.LongToString(cardInfo.getStartDate().getTime(), datePattern), DateUtil.LongToString(cardInfo.getEndDate().getTime(), datePattern)));
        textCardDesc.setText(cardInfo.getCardDesc());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
