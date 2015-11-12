package com.lovemoin.card.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.net.Register;
import com.lovemoin.card.app.net.SendCode;
import com.lovemoin.card.app.utils.CommonUtil;

/**
 * Created by zzt on 15-8-31.
 */
public class CompleteUserInfoActivity extends BaseActivity {
    private EditText editUserTel;
    private EditText editCode;
    private EditText editPassword;
    private EditText editRePassword;
    private Button btnSendMsg;
    private Button btnReg;
    private TextView textLoginAgreement;
    private CheckBox checkAgreement;

    private MoinCardApplication app;

    private CountDownTimer timer;

    private String userTel;
    private String userPwd;
    private String checkCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
        initData();
        initListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void initView() {
        editUserTel = (EditText) findViewById(R.id.edit_user_tel);
        editCode = (EditText) findViewById(R.id.edit_code);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRePassword = (EditText) findViewById(R.id.edit_re_password);
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        btnReg = (Button) findViewById(R.id.btn_register);
        textLoginAgreement = (TextView) findViewById(R.id.text_login_agreement);
        textLoginAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        checkAgreement = (CheckBox) findViewById(R.id.check_agreement);
    }

    private void initData() {
        app = (MoinCardApplication) getApplication();
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendMsg.setText(String.format("%d%s", millisUntilFinished / 1000, getString(R.string.second)));
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
                    new SendCode(userTel) {
                        @Override
                        public void onSuccess(String checkCode) {
                            btnSendMsg.setEnabled(false);
                            CompleteUserInfoActivity.this.checkCode = checkCode;
                            timer.start();
                        }

                        @Override
                        public void onFail(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    };
                }
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editUserTel.getText().toString().trim().equals(userTel)) {
                    Toast.makeText(getApplicationContext(), R.string.user_tel_has_been_changed_please_get_code_again, Toast.LENGTH_SHORT).show();
                    checkCode = null;
                    timer.cancel();
                    timer.onFinish();
                    return;
                }
                if (checkCode == null || !checkCode.equals(editCode.getText().toString())) {
                    editCode.setError(getString(R.string.invalid_code));
                    editCode.requestFocus();
                    return;
                }
                userPwd = editPassword.getText().toString().trim();
                if (userPwd.length() < 6) {
                    editPassword.setError(getString(R.string.invalid_pwd_length));
                    editPassword.requestFocus();
                    return;
                }
                if (!userPwd.equals(editRePassword.getText().toString().trim())) {
                    editRePassword.setError(getString(R.string.invalid_re_password));
                    editRePassword.requestFocus();
                    return;
                }
                if (!checkAgreement.isChecked()) {
                    new AlertDialog.Builder(CompleteUserInfoActivity.this)
                            .setTitle(R.string.hint)
                            .setMessage(R.string.unchecked_agreement)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                }
                new Register(userTel, userPwd, editCode.getText().toString(), app.getVersionName(), app.getCachedUserId()) {
                    @Override
                    public void onSuccess(String userTel, String id) {
                        Toast.makeText(getApplicationContext(), R.string.reg_success, Toast.LENGTH_SHORT).show();
                        app.cacheUserTel(userTel);
                        app.cachedUserId(id);
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });
        textLoginAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CompleteUserInfoActivity.this)
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
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
