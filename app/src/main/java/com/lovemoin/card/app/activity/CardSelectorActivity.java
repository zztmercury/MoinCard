package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardViewPagerAdapter;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.DaoMaster;
import com.lovemoin.card.app.db.DaoSession;
import com.lovemoin.card.app.widget.PlusStarDrawView;

import java.util.List;

/**
 * Created by zzt on 15-9-16.
 */
public class CardSelectorActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selector);
        initView();
        initDao();
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
        cardList = (List<CardInfo>) getIntent().getSerializableExtra(CARD_LIST);
        count = getIntent().getIntExtra(COUNT, -1);
        mAdapter = new CardViewPagerAdapter(getSupportFragmentManager());
        cardViewPager.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.addAll(cardList);
        starView.setCount(count);
    }

    private void initListener() {
        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starView.setCount(-1);
                cardList.get(0).setCurrentPoint(cardList.get(0).getCurrentPoint() + count);
//                cardInfoDao.insertOrReplace(cardList.get(0));
                cardInfoDao.update(cardList.get(0));
                starView.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                textHint.setText(R.string.get_point_success);
                textHint.setVisibility(View.VISIBLE);
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CardSelectorActivity.this, HomeActivity.class));
                    }
                });
            }
        });
    }


    private void initDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "moinCard.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        cardInfoDao = daoSession.getCardInfoDao();
    }
}
