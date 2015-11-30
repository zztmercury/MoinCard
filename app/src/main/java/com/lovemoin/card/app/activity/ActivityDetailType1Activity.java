package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.entity.ActivityType1;
import com.lovemoin.card.app.net.LoadActivityDetail;
import com.lovemoin.card.app.utils.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by zzt on 15-9-21.
 */
public class ActivityDetailType1Activity extends BaseActivityDetailActivity {
    private static final String DATE_PATTERN = "yyyy年M月d日hh:mm";

    private ImageView imgMain;
    private ImageView imgFlag;
    private TextView textName;
    private TextView textMerchant;
    private TextView textAddress;
    private TextView textDuration;
    private TextView textDetail;
    private View container;

    private String userId;
    private ImageLoader loader;

    private MoinCardApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.layoutAttend).setVisibility(View.GONE);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        imgFlag = (ImageView) findViewById(R.id.imgFlag);
        textName = (TextView) findViewById(R.id.textName);
        textMerchant = (TextView) findViewById(R.id.textMerchant);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textDuration = (TextView) findViewById(R.id.textDuration);
        textDetail = (TextView) findViewById(R.id.textDetail);
        container = findViewById(R.id.layoutContainer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        activityInfo = (ActivityInfo) getIntent().getSerializableExtra(Config.KEY_ACTIVITY);
        userId = app.getCachedUserId();
        loader = ImageLoader.getInstance();
        loadDetail();
    }

    private void loadDetail() {
        new LoadActivityDetail(activityInfo, userId) {
            @Override
            public void onSuccess(ActivityInfo completeActivityInfo) {
                bindDataToViews((ActivityType1) completeActivityInfo);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void bindDataToViews(final ActivityType1 activityInfo) {
        loader.displayImage(Config.SERVER_URL + "/moinbox/" + activityInfo.getImg(), imgMain, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                container.setBackgroundColor(Palette.from(loadedImage).generate().getVibrantColor(getResources().getColor(R.color.gray)));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        if (activityInfo.getIsOfficial())
            imgFlag.setImageResource(R.drawable.official_activity);
        textName.setText(activityInfo.getName());
        List<String> merchantList = activityInfo.getMerchantList();
        StringBuffer buffer = new StringBuffer();
        for (String merchantName : merchantList) {
            buffer.append(merchantName).append("，");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        textMerchant.setText(buffer.toString());
        textAddress.setText(activityInfo.getAddress());
        textDuration.setText(String.format("%s - %s",
                DateUtil.LongToString(activityInfo.getStartDate().getTime(), DATE_PATTERN),
                DateUtil.LongToString(activityInfo.getEndDate().getTime(), DATE_PATTERN)));
        textDetail.setText(activityInfo.getDetail());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
    }
}
