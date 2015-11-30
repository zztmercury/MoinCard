package com.lovemoin.card.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.lovemoin.card.app.MoinCardApplication;

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

        //startActivity(new Intent(this, ActivityCalendarActivity.class));
        //finish();
        //if (app.isFirstTimeInstalled()) {
        //    final EditText inputServer = new EditText(this);
        //    new AlertDialog.Builder(this)
        //            .setView(inputServer)
        //            .setMessage("请输入服务器IP")
        //            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
        //                @Override
        //                public void onClick(DialogInterface dialog, int which) {
        //                    Config.SERVER_URL = String.format("http://%s:8080", inputServer.getText().toString());
        //                    getSharedPreferences(MoinCardApplication.APP_NAME, MODE_PRIVATE).edit().putString("serverUrl", Config.SERVER_URL).commit();
        //                    enterApp();
        //                }
        //            })
        //            .setOnCancelListener(new DialogInterface.OnCancelListener() {
        //                @Override
        //                public void onCancel(DialogInterface dialog) {
        //                    finish();
        //                }
        //            }).show();
        //} else {
        //    Config.SERVER_URL = getSharedPreferences(MoinCardApplication.APP_NAME, MODE_PRIVATE).getString("serverUrl", String.format("http://%s:8080", "192.168.2.118"));
            enterApp();
        //}

    }

    private void enterApp() {
        if (app.isLogin() && app.getCachedUserId() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else
        //if (!app.isFirstTimeInstalled())
        {
            startActivity(new Intent(this, EntranceActivity.class));
            finish();
        }
        //else{
        //    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //    new LoginByImei(tm.getDeviceId(), app.getVersionName()) {
        //
        //        @Override
        //        public void onSuccess(String userId) {
        //            app.cachedUserId(userId);
        //            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        //            finish();
        //        }
        //
        //        @Override
        //        public void onFail(String message) {
        //            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        //            startActivity(new Intent(MainActivity.this, EntranceActivity.class));
        //            finish();
        //        }
        //    };
        //}
    }

}