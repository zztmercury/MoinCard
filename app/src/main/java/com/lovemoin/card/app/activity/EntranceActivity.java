package com.lovemoin.card.app.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.constant.ResultCode;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zzt on 15-10-12.
 */
public class EntranceActivity extends BaseActivity {
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WE_CHAT_APP_ID, true);
        iwxapi.registerApp(Config.WE_CHAT_APP_ID);
        setContentView(R.layout.activity_entrance);
        initListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (!iwxapi.isWXAppInstalled()) {
            findViewById(R.id.layout_other_login_method).setVisibility(View.GONE);
            findViewById(R.id.btn_we_chat_login).setVisibility(View.GONE);
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (app.isLogin() && !TextUtils.isEmpty(app.getCachedUserId()))
            finish();
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
        findViewById(R.id.btn_we_chat_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                iwxapi.sendReq(req);
                //finish();
            }
        });
    }
}
