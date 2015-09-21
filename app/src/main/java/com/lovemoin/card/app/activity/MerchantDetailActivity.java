package com.lovemoin.card.app.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.*;
import com.lovemoin.card.app.net.DeleteCard;
import com.lovemoin.card.app.net.LoadMerchantInfo;
import com.lovemoin.card.app.widget.RecyclerViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by zzt on 15-9-2.
 *
 * @author zzt
 */
public class MerchantDetailActivity extends AppCompatActivity {
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
    private View layoutCardDetail;
    private View layoutCardRecord;
    private View layoutAllStores;
    private RecyclerViewForScrollView listRecentActivity;

    private ImageLoader loader;

    private ProgressDialog pd;

    private boolean canConvert;

    private MoinCardApplication app;
    private CardInfoDao cardInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        loader = ImageLoader.getInstance();
        initDao();
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
        listRecentActivity = (RecyclerViewForScrollView) findViewById(R.id.listRecentActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        cardInfo = app.getCurrentCard();
        app.setIsExchange(false);
        pd = new ProgressDialog(this);
        bindCard();
        loadMerchantInfoFromServer();
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
        btnConvert.setEnabled(canConvert);
        textCurrentPoint.setText(String.format("%d枚", cardInfo.getCurrentPoint()));
        textObject.setText(cardInfo.getConvertObj());
        textCardCounter.setText(String.format("%d/%d", currentPoint, convertPoint));
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.setIsExchange(true);
                pd.setMessage(getString(R.string.find_device_hint));
                pd.show();
            }
        });

    }

    private void loadMerchantInfoFromServer() {
        new LoadMerchantInfo(cardInfo.getCardCode()) {
            @Override
            public void onSuccess(MerchantInfo merchantInfo) {
                MerchantDetailActivity.this.merchantInfo = merchantInfo;
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
                i.putExtra(Config.KEY_MERCHANT_ID, merchantInfo.getMerchantId());
                startActivity(i);
            }
        });
        imgMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MerchantDetailActivity.this, ImageWallActivity.class);
                i.putExtra(Config.KEY_MERCHANT_ID, merchantInfo.getMerchantId());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(MerchantDetailActivity.this)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                new DeleteCard(cardInfo.getCardCode().substring(cardInfo.getCardCode().length() - 16)) {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(getApplicationContext(), R.string.delete_success, Toast.LENGTH_LONG).show();
                                        cardInfoDao.delete(app.getCurrentCard());
                                        dialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    }
                                };
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
        } else
            super.onBackPressed();
    }


    private void initDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "moinCard.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        cardInfoDao = daoSession.getCardInfoDao();
    }
}
