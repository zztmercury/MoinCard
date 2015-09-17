package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardViewPagerAdapter;
import com.lovemoin.card.app.db.CardInfo;
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

    private CardViewPagerAdapter mAdapter;

    private Integer count;
    private List<CardInfo> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selector);
        initView();
        initData();
    }

    private void initView() {
        starView = (PlusStarDrawView) findViewById(R.id.starView);
        cardViewPager = (ViewPager) findViewById(R.id.pager);
        textHint = (TextView) findViewById(R.id.textHint);
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
}
