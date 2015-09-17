package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.CardRecordAdapter;
import com.lovemoin.card.app.entity.CardRecord;
import com.lovemoin.card.app.net.LoadCardRecord;

import java.util.List;

/**
 * Created by zzt on 15-9-14.
 */
public class CardRecordListActivity extends AppCompatActivity {
    private RecyclerView list;
    private TextView textCardRecord;
    private CardRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_record);
        initView();
        initData();
    }

    private void initView() {
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        textCardRecord = (TextView) findViewById(R.id.textCardRecord);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        mAdapter = new CardRecordAdapter(this);
        list.setAdapter(mAdapter);
        String userTel = ((MoinCardApplication) getApplication()).getCachedUserTel();
        String cardNumber = ((MoinCardApplication) getApplication()).getCurrentCard().getCardCode();
        new LoadCardRecord(userTel, cardNumber) {
            @Override
            public void onSuccess(List<CardRecord> recordList) {
                if (recordList.isEmpty()) {
                    textCardRecord.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.addAll(recordList);
                }
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(CardRecordListActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
