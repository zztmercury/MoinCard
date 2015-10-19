package com.lovemoin.card.app.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.ActivityNameListAdapter;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.MerchantInfo;
import com.lovemoin.card.app.net.DeleteCard;
import com.lovemoin.card.app.net.LoadActivityByMerchant;
import com.lovemoin.card.app.net.LoadMerchantInfo;
import com.lovemoin.card.app.widget.DividerItemDecoration;
import com.lovemoin.card.app.widget.LinearLayoutManagerForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by zzt on 15-9-2.
 *
 * @author zzt
 */
public class MerchantDetailActivity extends BaseActivity {
    /**
     * 卡片信息
     */
    private CardInfo cardInfo;
    /**
     * 商户信息
     */
    private MerchantInfo merchantInfo;

    /**
     * 卡图
     */
    private ImageView imgCard;
    /**
     * 积点计数图
     */
    private ImageView imgCardCount;
    /**
     * 当前积点
     */
    private TextView textCurrentPoint;
    /**
     * 还需积点
     */
    private TextView textNeededPoint;
    /**
     * 兑换物品
     */
    private TextView textObject;
    /**
     * 积点计数
     */
    private TextView textCardCounter;
    /**
     * 商户主图
     */
    private ImageView imgMerchant;
    /**
     * 商户名
     */
    private TextView textMerchantName;
    /**
     * 商户简介
     */
    private TextView textMerchantBrief;
    /**
     * 商户介绍
     */
    private TextView textMerchantDesc;
    /**
     * 兑换按钮
     */
    private Button btnConvert;
    private TextView textCardDetail;
    private TextView textCardRecord;
    private View layoutCardDetail;

    private View layoutCardRecord;
    private View layoutAllStores;
    private RecyclerView listRecentActivity;

    private ImageLoader loader;

    private boolean canConvert;

    private MoinCardApplication app;
    private CardInfoDao cardInfoDao;
    private ActivityNameListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        loader = ImageLoader.getInstance();
        initView();
        initData();
    }

    private void initView() {
        imgCard = (ImageView) findViewById(R.id.imgCard);
        imgCardCount = (ImageView) findViewById(R.id.imgCardCounter);
        textCurrentPoint = (TextView) findViewById(R.id.textCurrentPoint);
        textNeededPoint = (TextView) findViewById(R.id.textNeededPoint);
        textObject = (TextView) findViewById(R.id.textObject);
        textCardCounter = (TextView) findViewById(R.id.textCardCounter);
        imgMerchant = (ImageView) findViewById(R.id.imgMerchant);
        textMerchantName = (TextView) findViewById(R.id.textMerchantName);
        textMerchantBrief = (TextView) findViewById(R.id.textMerchantBrief);
        textMerchantDesc = (TextView) findViewById(R.id.textMerchantDesc);
        btnConvert = (Button) findViewById(R.id.btnExchange);
        layoutCardRecord = findViewById(R.id.layoutCardRecord);
        layoutCardDetail = findViewById(R.id.layoutCardDetail);
        layoutAllStores = findViewById(R.id.layoutAllStores);
        textCardDetail = (TextView) findViewById(R.id.textCardDetail);
        textCardRecord = (TextView) findViewById(R.id.textCardRecord);
        listRecentActivity = (RecyclerView) findViewById(R.id.listRecentActivity);
        listRecentActivity.setLayoutManager(new LinearLayoutManagerForScrollView(getApplicationContext()));
        listRecentActivity.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.default_divider));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        cardInfo = app.getCurrentCard();
        cardInfoDao = app.getCardInfoDao();
        app.setIsExchange(false);
        pd = new ProgressDialog(this);
        mAdapter = new ActivityNameListAdapter(this);
        listRecentActivity.setAdapter(mAdapter);
        bindCard();
        loadMerchantInfoFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindCard();
        if (cardInfo.getCardType().equals(CardInfo.TYPE_COUPON) && cardInfo.getCurrentPoint() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.hint_coupon_used_up)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            deleteCard(dialog);
                        }
                    }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    private void loadActivities() {
        new LoadActivityByMerchant(app.getCachedUserId(), 0, merchantInfo.getMerchantUUID()) {
            @Override
            public void onSuccess(List<ActivityInfo> activityList) {
                mAdapter.clear();
                mAdapter.addAll(activityList);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void bindCard() {
        loader.displayImage(Config.SERVER_URL + cardInfo.getCardImg(), imgCard, new ImageLoadingListener() {
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
        int currentPoint = cardInfo.getCurrentPoint();
        int convertPoint = cardInfo.getConvertPoint();
        canConvert = (currentPoint >= convertPoint);
        if (currentPoint >= convertPoint) {
            imgCardCount.setImageResource(R.drawable.q10);
            textNeededPoint.setText("兑换即获得");
        } else {
            int index = Math.round(currentPoint * 10f / convertPoint);
            imgCardCount.setImageResource(getResources().getIdentifier("q" + index, "drawable", getPackageName()));
            textNeededPoint.setText(String.format("再积%d个积点可兑换", convertPoint - currentPoint));
        }
        switch (cardInfo.getCardType()) {
            case CardInfo.TYPE_POINT:
                textCardDetail.setText(R.string.point_card_detail);
                break;
            case CardInfo.TYPE_COUPON:
                textCardDetail.setText(R.string.coupon_detail);
                break;
            case CardInfo.TYPE_SIGN:
                textCardDetail.setText(getString(R.string.sign_card_detail));
                break;
        }
        btnConvert.setEnabled(canConvert);
        textCurrentPoint.setText(String.format("%d枚", cardInfo.getCurrentPoint()));
        textObject.setText(cardInfo.getConvertObj());
        textObject.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textCardCounter.setText(String.format("%d/%d", currentPoint, convertPoint));
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nfcAdapter == null) {
                    final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(MerchantDetailActivity.this).create();
                    dialog.show();
                    dialog.setContentView(R.layout.dialog_no_nfc);
                    Window window = dialog.getWindow();
                    window.findViewById(R.id.btn_I_know).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            app.setFirstTimeInstalled(false);
                        }
                    });
                } else if (!nfcAdapter.isEnabled()) {
                    new AlertDialog.Builder(MerchantDetailActivity.this)
                            .setMessage(R.string.hint_enable_nfc)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    app.setIsExchange(true);
                    pd.setMessage(getString(R.string.find_device_hint));
                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            app.setIsExchange(false);
                        }
                    });
                    pd.show();
                }
            }
        });

    }

    private void loadMerchantInfoFromServer() {
        new LoadMerchantInfo(cardInfo.getCardCode()) {
            @Override
            public void onSuccess(MerchantInfo merchantInfo) {
                MerchantDetailActivity.this.merchantInfo = merchantInfo;
                loadActivities();
                bindData();
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(MerchantDetailActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void bindData() {
        loader.displayImage(Config.SERVER_URL + merchantInfo.getMainImg(), imgMerchant);
        textMerchantName.setText(merchantInfo.getMerchantName());
        textMerchantBrief.setText(merchantInfo.getBrief());
        textMerchantDesc.setText(merchantInfo.getDescription());
        layoutCardRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MerchantDetailActivity.this, CardRecordListActivity.class));
            }
        });
        layoutCardDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MerchantDetailActivity.this, CardDetailActivity.class));
            }
        });
        layoutAllStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantDetailActivity.this, StoreListActivity.class);
                i.putExtra(Config.KEY_MERCHANT_ID, merchantInfo.getMerchantUUID());
                startActivity(i);
            }
        });
        imgMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantDetailActivity.this, ImageWallActivity.class);
                i.putExtra(Config.KEY_MERCHANT_ID, merchantInfo.getMerchantUUID());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(MerchantDetailActivity.this)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                deleteCard(dialog);
                            }
                        })
                        .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_merchant_detail, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (pd.isShowing()) {
            pd.dismiss();
            app.setIsExchange(false);
        } else {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra(HomeActivity.KEY_SECTION, 0);
            startActivity(i);
            finish();
        }
    }

    private void deleteCard(final DialogInterface dialog) {
        new DeleteCard(cardInfo.getCardCode().substring(cardInfo.getCardCode().length() - 16)) {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), R.string.delete_success, Toast.LENGTH_LONG).show();
                cardInfoDao.delete(app.getCurrentCard());
                dialog.dismiss();
                onBackPressed();
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        };
    }
}
