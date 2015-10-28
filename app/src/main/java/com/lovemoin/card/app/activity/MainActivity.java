package com.lovemoin.card.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.net.LoginByImei;

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

//        startActivity(new Intent(this,ActivityCalendarActivity.class));
//        finish();
        if (app.isLogin() && app.getCachedUserId() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (!app.isFirstTimeInstalled()) {
            startActivity(new Intent(this, EntranceActivity.class));
            finish();
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            new LoginByImei(tm.getDeviceId(), app.getVersionName()) {

                @Override
                public void onSuccess(String userId) {
                    app.cachedUserId(userId);
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, EntranceActivity.class));
                }
            };
        }
    }

}