package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.db.CardInfo;

/**
 * Created by zzt on 15-9-2.
 */
public class MerchantDetailActivity extends AppCompatActivity {
    private CardInfo cardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
    }
}
