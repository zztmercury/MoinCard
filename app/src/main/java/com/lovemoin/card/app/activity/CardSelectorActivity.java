package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardViewPagerAdapter;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.widget.PlusStarDrawView;

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

    private CardViewPagerAdapter mAdapter;

    private Integer count;
    private List<CardInfo> cardList;
    private CardInfoDao cardInfoDao;

    private MoinCardApplication app;
    private int realCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selector);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        starView = (PlusStarDrawView) findViewById(R.id.starView);
        cardViewPager = (ViewPager) findViewById(R.id.pager);
        textHint = (TextView) findViewById(R.id.textHint);
        rootView = findViewById(R.id.rootView);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        cardInfoDao = app.getCardInfoDao();
        cardList = (List<CardInfo>) getIntent().getSerializableExtra(CARD_LIST);
        app.setCurrentCard(cardList.get(0));
        count = getIntent().getIntExtra(COUNT, -1);
        mAdapter = new CardViewPagerAdapter(getSupportFragmentManager(), count == -1);
        cardViewPager.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.addAll(cardList);
        if (count == -2) {
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void clickStar() {
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
}
