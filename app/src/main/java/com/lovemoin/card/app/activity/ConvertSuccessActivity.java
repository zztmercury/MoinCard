package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.db.CardInfo;

/**
 * Created by zzt on 15-9-17.
 */
public class ConvertSuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_success);
        setResult(EXCHANGE_SUCCESS);
        CardInfo cardInfo = app.getCurrentCard();
        ((TextView) findViewById(R.id.text_convert_object)).setText(cardInfo.getConvertObj());
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
