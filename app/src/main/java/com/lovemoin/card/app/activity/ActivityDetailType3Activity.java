package com.lovemoin.card.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.dto.CouponDto;
import com.lovemoin.card.app.entity.ActivityType3;
import com.lovemoin.card.app.net.AttendActivity;
import com.lovemoin.card.app.net.GetPrize;
import com.lovemoin.card.app.net.GetRelateMerchantCoupon;
import com.lovemoin.card.app.net.LoadActivityDetail;
import com.lovemoin.card.app.utils.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zzt on 15-9-2.
 */
public class ActivityDetailType3Activity extends BaseActivityDetailActivity {
    private static final String DATE_PATTERN = "yyyy年M月d日hh:mm";
    private String userId;
    private ImageLoader imageLoader;
    private MoinCardApplication app;

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
    private View cover;
    private TextView textGiftName;
    private ImageView imgGift;

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
        cover = findViewById(R.id.layoutCover);
        textGiftName = (TextView) findViewById(R.id.textGiftName);
        imgGift = (ImageView) findViewById(R.id.imgGift);
        findViewById(R.id.btnCloseCover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cover.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.btn_view_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponName = textGiftName.getText().toString();
                QueryBuilder<CardInfo> qb = app.getCardInfoDao().queryBuilder();
                qb.where(CardInfoDao.Properties.CardName.eq(couponName));
                CardInfo cardInfo = qb.unique();
                app.setCurrentCard(cardInfo);
                startActivity(new Intent(ActivityDetailType3Activity.this, MerchantDetailActivity.class));
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        activityInfo = (ActivityInfo) getIntent().getSerializableExtra(Config.KEY_ACTIVITY);
        userId = app.getCachedUserId();
        imageLoader = ImageLoader.getInstance();
        loadDetail();
    }

    private void loadDetail() {
        pd.setMessage(getString(R.string.loading));
        pd.show();
        new LoadActivityDetail(activityInfo, userId) {
            @Override
            public void onSuccess(ActivityInfo completeActivityInfo) {
                pd.dismiss();
                bindDataToViews((ActivityType3) completeActivityInfo);
            }

            @Override
            public void onFail(String message) {
                pd.dismiss();
                Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void bindDataToViews(final ActivityType3 activityInfo) {
        imageLoader.displayImage(Config.SERVER_URL + "/moinbox/" + "/" + activityInfo.getImg(), imgMain, new ImageLoadingListener() {
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
        if (activityInfo.getStartDate().getTime() <= System.currentTimeMillis() && System.currentTimeMillis() <= activityInfo.getEndDate().getTime()) {
            if (!activityInfo.getIsAttend()) {
                btnReact.setText(R.string.attend_now);
                btnReact.setEnabled(true);
                activityInfo.setCurrentStep(0);
                stepsView.setVisibility(View.GONE);
                btnReact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd.setMessage(getString(R.string.attending));
                        pd.show();
                        new AttendActivity(activityInfo.getActivityId(), userId, activityInfo.getMerchantIdList().get(0), activityInfo.getType(), activityInfo.getNum()) {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ActivityDetailType3Activity.this, R.string.attend_success, Toast.LENGTH_SHORT).show();
                                pd.dismiss();
//                            btnReact.setText(R.string.attended);
//                            btnReact.setEnabled(false);
//                            stepsView.setVisibility(View.VISIBLE);
                                loadDetail();
                            }

                            @Override
                            public void onFail(String message) {
                                Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_SHORT).show();
                                pd.dismiss();
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
                                    showPrizeSelectDialog(activityInfo);
                                    break;
                                default:
                                    getGift(activityInfo, null, null);
                                    break;
                            }
                        }
                    });
                } else {
                    btnReact.setText(R.string.completed);
                    btnReact.setEnabled(false);
                }
            }
        } else {
            btnReact.setEnabled(false);
            if (System.currentTimeMillis() < activityInfo.getStartDate().getTime())
                btnReact.setText(getString(R.string.about_to_begin));
            else
                btnReact.setText(getString(R.string.activity_end));
        }
        stepsView.setLabels(activityInfo.getStepText().split("\\|"))
                .setProgressColorIndicator(getResources().getColor(R.color.light_orange))
                .setBarColorIndicator(getResources().getColor(R.color.light_gray))
                .setLabelColorIndicator(getResources().getColor(R.color.gray))
                .setCompletedPosition(activityInfo.getCurrentStep())
                .drawView();
        textAddress.setText(activityInfo.getAddress());
    }


    private void showPrizeSelectDialog(final ActivityType3 activityInfo) {
        new GetRelateMerchantCoupon(activityInfo.getActivityId()) {
            private int selectedCouponIndex = 0;

            @Override
            public void onSuccess(final List<CouponDto> couponList) {
                String[] couponNames = new String[couponList.size()];
                for (int i = 0; i < couponList.size(); i++) {
                    couponNames[i] = couponList.get(i).getCouponName();
                }
                new AlertDialog.Builder(ActivityDetailType3Activity.this)
                        .setTitle(R.string.please_select_coupon)
                        .setSingleChoiceItems(couponNames, selectedCouponIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedCouponIndex = which;
                            }
                        })
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!couponList.isEmpty())
                                    getGift(activityInfo, couponList.get(selectedCouponIndex).getCouponId(), couponList.get(selectedCouponIndex).getMerchantId());
                            }
                        }).show();
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getGift(final ActivityType3 activityInfo, @Nullable String couponId, @Nullable String merchantId) {
        pd.setMessage(getString(R.string.getting_gift));
        pd.show();
        new GetPrize(activityInfo.getActivityId(), userId, activityInfo.getType(), activityInfo.getNum(), merchantId, couponId) {

            @Override
            public void onSuccess(String img, String giftName) {
                pd.dismiss();
                cover.setVisibility(View.VISIBLE);
                textGiftName.setText(giftName);
                imageLoader.displayImage(Config.SERVER_URL + "/moinbox/" + img, imgGift);
                app.updateCardInfoFromServer(false);
                loadDetail();
            }

            @Override
            public void onFail(String message) {
                pd.dismiss();
                Toast.makeText(ActivityDetailType3Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }


    @Override
    public void onBackPressed() {
        if (pd.isShowing()) {
            pd.dismiss();
        } else if (cover.getVisibility() != View.GONE) {
            cover.setVisibility(View.GONE);
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
