package com.lovemoin.card.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lovemoin.card.app.MoinCardApplication;

/**
 * Created by zzt on 15-8-24.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoinCardApplication app = (MoinCardApplication) getApplication();
//        System.out.println(((MoinCardApplication) getApplication()).getCachedUserTel());
        if (app.isLogin() && app.getCachedUserId() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
//        startActivity(new Intent(this,ActivityDetailType3Activity.class));
    }
}
