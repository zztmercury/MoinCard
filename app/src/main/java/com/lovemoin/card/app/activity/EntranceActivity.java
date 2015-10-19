package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.ResultCode;

/**
 * Created by zzt on 15-10-12.
 */
public class EntranceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        initListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initListener() {
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EntranceActivity.this, RegisterActivity.class), 100);
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EntranceActivity.this, LoginActivity.class), 100);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == ResultCode.LOGIN_SUCCESS || resultCode == ResultCode.REGISTER_SUCCESS) {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                    break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
