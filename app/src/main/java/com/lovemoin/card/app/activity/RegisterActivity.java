package com.lovemoin.card.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.net.Register;
import com.lovemoin.card.app.utils.CommonUtil;

/**
 * Created by zzt on 15-8-31.
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText editUserTel;
    private EditText editCode;
    private EditText editPassword;
    private EditText editRePassword;
    private Button btnSendMsg;
    private Button btnReg;
    private CheckBox checkAgreement;
    private TextView textLoginAgreement;

    private MoinCardApplication app;

    private CountDownTimer timer;

    private String userTel;
    private String userPwd;
    private String checkCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        editUserTel = (EditText) findViewById(R.id.editUserTel);
        editCode = (EditText) findViewById(R.id.editCode);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editRePassword = (EditText) findViewById(R.id.editRePassword);
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        btnReg = (Button) findViewById(R.id.btnReg);
        checkAgreement = (CheckBox) findViewById(R.id.checkAgreement);
        textLoginAgreement = (TextView) findViewById(R.id.textLoginAgreement);
        textLoginAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendMsg.setText(millisUntilFinished / 1000 + getString(R.string.second));
            }

            @Override
            public void onFinish() {
                btnSendMsg.setText(R.string.re_send);
                btnSendMsg.setEnabled(true);
            }
        };
    }

    private void initListener() {
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editUserTel.getText()) || !CommonUtil.isMobile(editUserTel.getText().toString())) {
                    editUserTel.setError(getString(R.string.invalid_tel_hint));
                    editUserTel.requestFocus();
                } else {
                    userTel = editUserTel.getText().toString().trim();
//                    new SendCode(userTel) {
//                        @Override
//                        public void onSuccess(String checkCode) {
//                            btnSendMsg.setEnabled(false);
//                            RegisterActivity.this.checkCode = checkCode;
//                            timer.start();
//                        }
//
//                        @Override
//                        public void onFail(String message) {
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                        }
//                    };
                }
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editUserTel.getText().toString().trim().equals(userTel)) {
                    Toast.makeText(getApplicationContext(), R.string.user_tel_has_been_changed_please_get_code_again, Toast.LENGTH_LONG).show();
                    checkCode = null;
                    timer.cancel();
                    timer.onFinish();
                    return;
                }
//                if (checkCode == null || !checkCode.equals(editCode.getText().toString())) {
//                    editCode.setError(getString(R.string.invalid_code));
//                    editCode.requestFocus();
//                    return;
//                }
                userPwd = editPassword.getText().toString();
                if (userPwd.length() < 6) {
                    editPassword.setError(getString(R.string.invalid_pwd_length));
                    editPassword.requestFocus();
                    return;
                }
                if (!userPwd.equals(editRePassword.getText().toString())) {
                    editRePassword.setError(getString(R.string.invalid_re_password));
                    editRePassword.requestFocus();
                    return;
                }
                if (!checkAgreement.isChecked()) {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.hint)
                            .setMessage(R.string.uncheck_agreemend)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                }
                new Register(userTel, userPwd) {
                    @Override
                    public void onSuccess(String userTel, String id) {
                        Toast.makeText(getApplicationContext(), R.string.reg_success, Toast.LENGTH_LONG).show();
                        app.cacheUserTel(userTel);
                        app.cachedUserId(id);
                        app.cacheLoginStatus(true);
                        startActivity(new Intent(RegisterActivity.this, GuideActivity.class));
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                };
            }
        });
        textLoginAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle(R.string.service_agreement)
                        .setMessage(R.string.agreement_content)
                        .setPositiveButton(R.string.known, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
