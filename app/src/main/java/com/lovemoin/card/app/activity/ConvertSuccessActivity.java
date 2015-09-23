package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.lovemoin.card.app.R;

/**
 * Created by zzt on 15-9-17.
 */
public class ConvertSuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_success);
        findViewById(R.id.layoutContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
}
