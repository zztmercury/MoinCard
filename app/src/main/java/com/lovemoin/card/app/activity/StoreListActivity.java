package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.StoreListAdapter;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.StoreInfo;
import com.lovemoin.card.app.net.LoadStoreList;

import java.util.List;

/**
 * Created by zzt on 15-9-14.
 */
public class StoreListActivity extends AppCompatActivity {
    private TextView textStoreCount;
    private RecyclerView list;
    private StoreListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        initView();
        initData();
    }

    private void initView() {
        textStoreCount = (TextView) findViewById(R.id.textStoreCount);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        mAdapter = new StoreListAdapter(this);
        list.setAdapter(mAdapter);
        String merchantId = getIntent().getStringExtra(Config.KEY_MERCHANT_ID);
        new LoadStoreList(merchantId) {
            @Override
            public void onSuccess(List<StoreInfo> storeList) {
                textStoreCount.setText(String.format(getString(R.string.stores_in_all), storeList.size()));
                mAdapter.addAll(storeList);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(StoreListActivity.this, message, Toast.LENGTH_LONG).show();
                textStoreCount.setText(String.format(getString(R.string.stores_in_all), 0));
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
