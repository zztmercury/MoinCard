package com.lovemoin.card.app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.*;
import com.anton46.stepsview.StepsView;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.entity.ActivityType3;
import com.lovemoin.card.app.net.AttendActivity;
import com.lovemoin.card.app.net.GetPrize;
import com.lovemoin.card.app.net.LoadActivityDetail;
import com.lovemoin.card.app.utils.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by zzt on 15-9-2.
 */
public class ActivityDetailType3Activity extends AppCompatActivity {
    private static final String DATE_PATTERN = "yyyy年M月d日hh:mm";
    private ActivityInfo activityInfo;
    private String userId;
    private ImageLoader imageLoader;
    private LinearLayout container;
    private ImageView imgMain;
    private ImageView imgFlag;
    private TextView textName;
    private TextView textMerchant;
    private TextView textAddress;
    private TextView textDuration;
    private TextView textMemberCount;
    private TextView textDetail;
    private Button btnReact;
    private StepsView stepsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);

        initView();
        initData();


//        String[] steps = {"step1", "step2", "step3", "step4", "step5"};
//        StepsView mStepsView = (StepsView) findViewById(R.id.steps);
//        mStepsView.setLabels(steps)
//                .setProgressColorIndicator(getResources().getColor(R.color.light_orange))
//                .setBarColorIndicator(getResources().getColor(R.color.light_gray))
//                .setLabelColorIndicator(getResources().getColor(R.color.gray))
//                .setCompletedPosition(2)
//                .drawView();
//        container.setBackgroundColor(Palette.from(bitmap).generate().getLightVibrantColor(getResources().getColor(R.color.gray)));
    }

    private void initView() {
        container = (LinearLayout) findViewById(R.id.layoutContainer);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        imgFlag = (ImageView) findViewById(R.id.imgFlag);
        textName = (TextView) findViewById(R.id.textName);
        textMerchant = (TextView) findViewById(R.id.textMerchant);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textDuration = (TextView) findViewById(R.id.textDuration);
        textMemberCount = (TextView) findViewById(R.id.textMemberCount);
        textDetail = (TextView) findViewById(R.id.textDetail);
        btnReact = (Button) findViewById(R.id.btnReact);
        stepsView = (StepsView) findViewById(R.id.steps);
    }

    private void initData() {
        activityInfo = (ActivityInfo) getIntent().getSerializableExtra(Config.KEY_ACTIVITY);
        userId = ((MoinCardApplication) getApplication()).getCachedUserId();
        imageLoader = ImageLoader.getInstance();
        loadDetail();
    }

    private void loadDetail() {
        new LoadActivityDetail(activityInfo, userId) {
            @Override
            public void onSuccess(ActivityInfo completeActivityInfo) {
                bindDataToViews((ActivityType3) completeActivityInfo);

            }

            @Override
            public void onFail(String message) {
                Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void bindDataToViews(final ActivityType3 activityInfo) {
        imageLoader.displayImage(Config.SERVER_URL + activityInfo.getImg(), imgMain, new ImageLoadingListener() {
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
        textDuration.setText(String.format("%s - %s",
                DateUtil.LongToString(activityInfo.getStartDate().getTime(), DATE_PATTERN),
                DateUtil.LongToString(activityInfo.getEndDate().getTime(), DATE_PATTERN)));
        textMemberCount.setText(String.valueOf(activityInfo.getMemberCount()));
        textDetail.setText(activityInfo.getDetail());
        if (!activityInfo.getIsAttend()) {
            btnReact.setText(R.string.attend_now);
            btnReact.setEnabled(true);
            activityInfo.setCurrentStep(0);
            stepsView.setVisibility(View.GONE);
            btnReact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AttendActivity(activityInfo.getActivityId(), userId, activityInfo.getType(), activityInfo.getNum()) {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ActivityDetailType3Activity.this, R.string.attend_success, Toast.LENGTH_LONG).show();
                            btnReact.setText(R.string.attended);
                            btnReact.setEnabled(false);
                            stepsView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFail(String message) {
                            Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_LONG).show();
                        }
                    };
                }
            });
        } else {
            stepsView.setVisibility(View.VISIBLE);
            if (activityInfo.getCurrentStep() < activityInfo.getTotalStep() - 2) {
                btnReact.setText(R.string.attended);
                btnReact.setEnabled(false);
            } else if (activityInfo.getCurrentStep() == activityInfo.getTotalStep() - 2) {
                btnReact.setText(R.string.get_prize);
                btnReact.setEnabled(true);
                btnReact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (activityInfo.getNum()) {
                            case 1:

                                break;
                            default:
                                new GetPrize(activityInfo.getActivityId(), userId, activityInfo.getType(), activityInfo.getNum(), null, null) {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(ActivityDetailType3Activity.this, R.string.extrange_success, Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_LONG).show();
                                    }
                                };
                                break;
                        }
                    }
                });
            } else {
                btnReact.setText(R.string.completed);
                btnReact.setEnabled(false);
            }
        }
        stepsView.setLabels(activityInfo.getStepText().split("\\|"))
                .setProgressColorIndicator(getResources().getColor(R.color.light_orange))
                .setBarColorIndicator(getResources().getColor(R.color.light_gray))
                .setLabelColorIndicator(getResources().getColor(R.color.gray))
                .setCompletedPosition(activityInfo.getCurrentStep())
                .drawView();
        textAddress.setText(R.string.see_in_merchant_detail);
    }


//    private void show
}
