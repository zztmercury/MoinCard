package com.lovemoin.card.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.lovemoin.card.app.db.ActivityInfoDao;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.GiftPackInfo;
import com.lovemoin.card.app.dto.CouponDto;
import com.lovemoin.card.app.net.GetGift;
import com.lovemoin.card.app.net.GetGiftForNewUserByCode;
import com.lovemoin.card.app.net.GetPrize;
import com.lovemoin.card.app.net.GetRelateMerchantCoupon;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zzt on 15-10-13.
 */
public class GiftPackActivity extends BaseActivity {

    public static final int TYPE_NEW_USER = 1;
    public static final int TYPE_DEFAULT = 0;
    public static final String TYPE = "type";

    private Button btnGet;
    private EditText editCode;
    private TextView textClickHere;
    private TextView textGiftComment;
    private GiftPackInfo giftPackInfo;
    private int type;
    private View cover;
    private TextView textGiftName;
    private ImageView imgGift;

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_pack);

        imageLoader = ImageLoader.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        bindListeners();
    }

    private void initView() {

        btnGet = (Button) findViewById(R.id.btn_get);
        editCode = (EditText) findViewById(R.id.edit_code);
        textClickHere = (TextView) findViewById(R.id.text_click_here);
        textGiftComment = (TextView) findViewById(R.id.text_gift_comment);

        type = getIntent().getIntExtra(TYPE, TYPE_DEFAULT);

        cover = findViewById(R.id.layoutCover);
        textGiftName = (TextView) findViewById(R.id.textGiftName);
        imgGift = (ImageView) findViewById(R.id.imgGift);

        if (type == TYPE_NEW_USER) {
            textGiftComment.setText(R.string.new_user_gift_comment);
        } else {
            giftPackInfo = (GiftPackInfo) getIntent().getSerializableExtra(Config.KEY_GIFT_PACK);
            textGiftComment.setText(giftPackInfo.getComment());
        }

        textClickHere.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }


    private void bindListeners() {
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnGet();
            }
        });
        textClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrizeSelectDialog(getIntent().getStringExtra(Config.KEY_ACTIVITY_ID));
            }
        });
        findViewById(R.id.btnCloseCover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cover.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.btnCloseCover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_NEW_USER) {
                    ActivityInfoDao dao = app.getActivityInfoDao();
                    ActivityInfo activityInfo = dao.load(getIntent().getStringExtra(Config.KEY_ACTIVITY_ID));
                    Intent intent = new Intent(GiftPackActivity.this, ActivityDetailType3Activity.class);
                    intent.putExtra(Config.KEY_ACTIVITY, activityInfo);
                    startActivity(intent);
                } else {
                    finish();
                }
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
                startActivity(new Intent(GiftPackActivity.this, MerchantDetailActivity.class));
                finish();
            }
        });
    }

    private void onClickBtnGet() {
        if (type == TYPE_NEW_USER || giftPackInfo.getNeedCode()) {
            editCode.setVisibility(View.VISIBLE);
            btnGet.setText(R.string.get);
            textGiftComment.setText(R.string.hint_with_no_gift_code);
            textClickHere.setVisibility(View.VISIBLE);
            btnGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(editCode.getText())) {
                        editCode.setError(getString(R.string.gift_code_can_not_be_empty));
                        return;
                    }
                    getGiftByCode();
                }
            });
        } else {
            getGift();
        }
    }

    private void getGiftByCode() {
        if (type == TYPE_NEW_USER) {
            new GetGiftForNewUserByCode(editCode.getText().toString()) {
                @Override
                public void onSuccess(String couponId) {
                    cover.setVisibility(View.VISIBLE);
                    getNewUserCoupon(couponId);
                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            };
        } else {
            getGift();
        }
    }

    private void getNewUserCoupon(String couponId) {
        String activityId = getIntent().getStringExtra(Config.KEY_ACTIVITY_ID);
        new GetPrize(activityId, app.getCachedUserId(), 3, 1, null, couponId) {
            @Override
            public void onSuccess(String giftImg, String giftName) {
                pd.dismiss();
                cover.setVisibility(View.VISIBLE);
                textGiftName.setText(giftName);
                imageLoader.displayImage(Config.SERVER_URL + giftImg, imgGift);
                app.updateCardInfoFromServer(false);
            }

            @Override
            public void onFail(String message) {

            }
        };
    }

    private void getGift() {
        new GetGift(app.getCachedUserId(), giftPackInfo.getGiftId(), editCode.getText().toString()) {
            @Override
            public void onSuccess(String giftName, String giftImg, int pointAddNum) {
                cover.setVisibility(View.VISIBLE);
                textGiftName.setText(giftName);
                if (pointAddNum == 0) {
                    imageLoader.displayImage(Config.SERVER_URL + giftImg, imgGift);
                } else {
                    TextView textGiftHint = (TextView) findViewById(R.id.text_gift_hint);
                    textGiftHint.setTextColor(Color.parseColor("#cc3333"));
                    textGiftHint.setTextSize(30);
                    textGiftHint.setText(String.format(getString(R.string.point_num_plus), pointAddNum));
                    imgGift.setImageResource(R.drawable.rocket_point);
                }
                app.updateCardInfoFromServer(false);
                app.getGiftPackInfoDao().delete(giftPackInfo);
            }

            @Override
            public void onFail(String message) {

            }
        };
    }

    private void showPrizeSelectDialog(final String activityId) {
        if (type == TYPE_NEW_USER)
            new GetRelateMerchantCoupon(activityId) {
                private int selectedCouponIndex = 0;

                @Override
                public void onSuccess(final List<CouponDto> couponList) {
                    String[] couponNames = new String[couponList.size()];
                    for (int i = 0; i < couponList.size(); i++) {
                        couponNames[i] = couponList.get(i).getCouponName();
                    }
                    new AlertDialog.Builder(GiftPackActivity.this)
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
                                        getNewUserCoupon(couponList.get(selectedCouponIndex).getCouponId());
                                }
                            }).show();
                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(GiftPackActivity.this, message, Toast.LENGTH_LONG).show();
                }
            };
        else {
            new AlertDialog.Builder(GiftPackActivity.this)
                    .setMessage(R.string.hint_no_code)
                    .setPositiveButton(R.string.known, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gift_pack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_give_up:
                new AlertDialog.Builder(this)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void giveUpGift() {

    }
}
