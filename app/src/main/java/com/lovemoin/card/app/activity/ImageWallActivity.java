package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.adapter.ImageWallAdapter;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ImageInfo;
import com.lovemoin.card.app.net.LoadMerchantImageSet;

import java.util.List;

/**
 * Created by zzt on 15-9-14.
 */
public class ImageWallActivity extends AppCompatActivity {
    private RecyclerView list;
    private ImageWallAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constant_list);
        initView();
        initData();
    }

    private void initView() {
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        mAdapter = new ImageWallAdapter(this);
        list.setAdapter(mAdapter);
        String merchantId = getIntent().getStringExtra(Config.KEY_MERCHANT_ID);
        new LoadMerchantImageSet(merchantId) {
            @Override
            public void onSuccess(List<ImageInfo> imageList) {
                mAdapter.addAll(imageList);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(ImageWallActivity.this, message, Toast.LENGTH_LONG).show();
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
