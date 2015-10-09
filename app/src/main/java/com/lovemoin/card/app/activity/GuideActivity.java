package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.lovemoin.card.app.R;

/**
 * Created by zzt on 15-9-21.
 */
public class GuideActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViewById(R.id.imgGuide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
