package com.lovemoin.card.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;

/**
 * Created by zzt on 15-8-24.
 * 程序主入口
 *
 * @author zzt
 */
public class MainActivity extends Activity {
    private MoinCardApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MoinCardApplication) getApplication();

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!app.isFirstTimeInstalled() && nfcAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.device_do_not_support_nfc, Toast.LENGTH_LONG).show();
        }
//        startActivity(new Intent(this,ActivityCalendarActivity.class));
//        finish();
        if (app.isLogin() && app.getCachedUserId() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, EntranceActivity.class));
            finish();
        }
    }

}