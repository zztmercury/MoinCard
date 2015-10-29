package com.lovemoin.card.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.ResultCode;
import com.lovemoin.card.app.net.Login;

/**
 * Created by zzt on 15-8-25.
 */
public class LoginActivity extends BaseActivity {
    private EditText editUserTel;
    private EditText editPassword;
    private MoinCardApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (MoinCardApplication) getApplication();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        editUserTel = (EditText) findViewById(R.id.edit_user_tel);
        editPassword = (EditText) findViewById(R.id.edit_password);
        findViewById(R.id.text_find_back_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(R.string.msg_modify_pwd_desc)
                        .setTitle(R.string.hint)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EntranceActivity.class));
                finish();
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void login() {
        if (checkValue()) {
            new Login(editUserTel.getText().toString(), editPassword.getText().toString(), app.getVersionName()) {
                @Override
                public void onSuccess(String userTel, String userId) {
                    app.cacheUserTel(userTel);
                    app.cachedUserId(userId);
                    app.cacheLoginStatus(true);
                    setResult(ResultCode.LOGIN_SUCCESS);
                    finish();
                }

                @Override
                public void onFail(String message) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private boolean checkValue() {
        if (TextUtils.isEmpty(editUserTel.getText())) {
            editUserTel.setError(getString(R.string.user_tel_can_not_be_empty));
            return false;
        }
//        if (TextUtils.isEmpty(editPassword.getText())) {
//            editPassword.setError(getString(R.string.password_can_not_be_empty));
//            return false;
//        }
        return true;
    }

}
