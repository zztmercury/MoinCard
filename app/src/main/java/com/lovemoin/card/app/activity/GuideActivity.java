package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;

/**
 * Created by zzt on 15-9-21.
 */
public class GuideActivity extends BaseActivity {
    private ImageView imgGuide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        imgGuide = (ImageView) findViewById(R.id.imgGuide);
        switch (app.getConnectMode()) {
            case MoinCardApplication.MODE_BLUETOOTH:
                imgGuide.setImageResource(R.drawable.help_bluetooth_guide);
                break;
            case MoinCardApplication.MODE_NFC:
                imgGuide.setImageResource(R.drawable.help_nfc_guide);
                break;
        }
        imgGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
