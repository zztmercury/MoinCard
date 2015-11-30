package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardViewPagerAdapter;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.ActivityInfoDao;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.net.LoadActivityList;
import com.lovemoin.card.app.widget.PlusStarDrawView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by zzt on 15-9-16.
 */
public class CardSelectorActivity extends BaseActivity {
    public static final String CARD_LIST = "cardList";
    public static final String COUNT = "count";
    private PlusStarDrawView starView;
    private ViewPager cardViewPager;
    private TextView textHint;
    private View rootView;
    private View shareActivityView;
    private TextView textActivityName;

    private CardViewPagerAdapter mAdapter;

    private Integer count;
    private List<CardInfo> cardList;
    private CardInfoDao cardInfoDao;
    private String activityId;
    private String shareUrl;
    private String activityTitle;

    private MoinCardApplication app;
    private int realCount;

    private IWXAPI iwxapi;
    private ActivityInfo activityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selector);

        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WE_CHAT_APP_ID);

        initView();
        initData();
        initListener();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getWindow().addFlags(Window.FEATURE_ACTION_BAR_OVERLAY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            actionBar.setBackgroundDrawable(null);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iwxapi.registerApp(Config.WE_CHAT_APP_ID);
    }

    @Override
    protected void onStop() {
        iwxapi.unregisterApp();
        super.onStop();
    }

    private void initView() {
        starView = (PlusStarDrawView) findViewById(R.id.starView);
        cardViewPager = (ViewPager) findViewById(R.id.pager);
        textHint = (TextView) findViewById(R.id.textHint);
        rootView = findViewById(R.id.rootView);
        shareActivityView = findViewById(R.id.layout_share_activity_of_merchant);
        textActivityName = (TextView) findViewById(R.id.text_activity_name);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        cardInfoDao = app.getCardInfoDao();
        cardList = (List<CardInfo>) getIntent().getSerializableExtra(CARD_LIST);
        app.setCurrentCard(cardList.get(0));
        count = getIntent().getIntExtra(COUNT, -1);

        activityId = getIntent().getStringExtra(Config.KEY_ACTIVITY_ID);
        shareUrl = getIntent().getStringExtra(Config.KEY_SHARE_URL);
        activityTitle = getIntent().getStringExtra(Config.KEY_TITLE);
        textActivityName.setText(activityTitle);

        mAdapter = new CardViewPagerAdapter(getSupportFragmentManager(), count == -1);
        cardViewPager.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.addAll(cardList);
        if (count == -2) {
            if (checkShareSupport() && activityId != null && shareUrl != null && activityTitle != null)
                shareActivityView.setVisibility(View.VISIBLE);
            starView.setCount(-1);
            textHint.setVisibility(View.VISIBLE);
            textHint.setText(getString(R.string.sign_in_success));
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CardSelectorActivity.this, MerchantDetailActivity.class));
                    finish();
                }
            });
        } else {
            starView.setCount(count);
            if (app.isUserFirstTime() && count != -1) {
                findViewById(R.id.layoutClickHint).setVisibility(View.VISIBLE);
            }
        }

    }

    private void initListener() {
        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar();
            }
        });
        cardViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                app.setCurrentCard(cardList.get(position));
            }
        });
        findViewById(R.id.layoutClickHint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar();
            }
        });
        textActivityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ActivityInfoDao activityInfoDao = app.getActivityInfoDao();
                activityInfo = activityInfoDao.load(activityId);
                if (activityInfo != null) {
                    openActivityDetail(activityInfo);
                } else {
                    new LoadActivityList(LoadActivityList.TYPE_RELATED, app.getCachedUserId(), 0) {
                        @Override
                        public void onSuccess(List<ActivityInfo> activityInfoList) {
                            activityInfoDao.deleteAll();
                            activityInfoDao.insertInTx(activityInfoList);
                            openActivityDetail(activityInfoDao.load(activityId));
                        }

                        @Override
                        public void onFail(String message) {

                        }
                    };
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void clickStar() {
        if (checkShareSupport() && activityId != null && shareUrl != null && activityTitle != null)
            shareActivityView.setVisibility(View.VISIBLE);
        starView.setCount(-1);
        app.setUserFirstTime(false);
        realCount = Math.min(count, app.getCurrentCard().getMaxPoint() - app.getCurrentCard().getCurrentPoint());
        app.getCurrentCard().setCurrentPoint(app.getCurrentCard().getCurrentPoint() + realCount);
        cardInfoDao.insertOrReplace(app.getCurrentCard());
//                cardInfoDao.insertOrReplace(cardList.get(0));
        starView.setVisibility(View.GONE);
        findViewById(R.id.layoutClickHint).setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        if (realCount != count) {
            textHint.setText("积点数已超过上限\n本次积点" + realCount + "枚");
        } else {
            textHint.setText(R.string.get_point_success);
        }
        textHint.setVisibility(View.VISIBLE);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardSelectorActivity.this, MerchantDetailActivity.class));
                finish();
            }
        });
    }

    private void openActivityDetail(ActivityInfo activityInfo) {
        if (activityInfo != null) {
            Intent i = new Intent();
            i.putExtra(Config.KEY_ACTIVITY, activityInfo);
            switch (activityInfo.getType()) {
                case 1:
                    i.setClass(this, ActivityDetailType1Activity.class);
                    break;
                case 3:
                    i.setClass(this, ActivityDetailType3Activity.class);
                    break;
                case 4:
                    i.setClass(this, ActivityDetailType4Activity.class);
                    break;
                default:
                    return;
            }
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (checkShareSupport() && !TextUtils.isEmpty(shareUrl)) {
            getMenuInflater().inflate(R.menu.menu_share, menu);
            if (!iwxapi.isWXAppInstalled()) {
                menu.getItem(R.id.action_share_to_we_chat_friend).setVisible(false);
                menu.getItem(R.id.action_share_to_we_chat_timeline).setVisible(false);
            }
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            final ActivityInfoDao activityInfoDao = app.getActivityInfoDao();
            activityInfo = activityInfoDao.load(activityId);
            if (activityInfo != null) {
                switch (item.getItemId()) {
                    case R.id.action_share_to_we_chat_friend:
                        shareToWeChat(SendMessageToWX.Req.WXSceneSession);
                        break;
                    case R.id.action_share_to_we_chat_timeline:
                        shareToWeChat(SendMessageToWX.Req.WXSceneTimeline);
                        break;
                }
            } else {
                new LoadActivityList(LoadActivityList.TYPE_RELATED, app.getCachedUserId(), 0) {
                    @Override
                    public void onSuccess(List<ActivityInfo> activityInfoList) {
                        activityInfoDao.deleteAll();
                        activityInfoDao.insertInTx(activityInfoList);
                        switch (item.getItemId()) {
                            case R.id.action_share_to_we_chat_friend:
                                shareToWeChat(SendMessageToWX.Req.WXSceneSession);
                                break;
                            case R.id.action_share_to_we_chat_timeline:
                                shareToWeChat(SendMessageToWX.Req.WXSceneTimeline);
                                break;
                        }
                    }

                    @Override
                    public void onFail(String message) {

                    }
                };
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareToWeChat(int WXScene) {
        if (activityInfo == null) {
            return;
        }
        WXWebpageObject object = new WXWebpageObject();
        object.webpageUrl = activityInfo.getShareUrl();

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = object;
        message.title = activityInfo.getName();
        message.description = activityInfo.getBrief();

        app.setCurrentActivityId(activityInfo.getActivityId());

        Bitmap bitmap = null;
        try {
            bitmap = ImageLoader.getInstance().loadImageSync(Config.SERVER_URL + "/moinbox/" + activityInfo.getBriefImg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        bitmap.recycle();
        message.setThumbImage(thumbBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        req.scene = WXScene;

        iwxapi.sendReq(req);
    }

    private boolean checkShareSupport() {
        if (iwxapi != null)
            return iwxapi.isWXAppInstalled();
        return false;
    }

}
