package com.lovemoin.card.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.net.ModifyUser;

/**
 * Created by zzt on 15-9-21.
 */
public class UserModifyActivity extends BaseActivity {
    private EditText editOldPassword, editNewPassword, editConfirmPassword;

    private String oldPassword;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);
        initView();
    }

    private void initView() {
        editOldPassword = (EditText) findViewById(R.id.editOldPassword);
        editNewPassword = (EditText) findViewById(R.id.editNewPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveModify();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveModify() {
        if (validInput()) {
            pd.setMessage(getString(R.string.hint_modify_user));
            new ModifyUser(app.getCachedUserTel(), newPassword, oldPassword) {
                @Override
                public void onSuccess() {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.hint_modify_user_success, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFail(String message) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            };
        }
    }

    private boolean validInput() {
        oldPassword = editOldPassword.getText().toString().trim();
        newPassword = editNewPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        if (oldPassword.length() < 6) {
            editOldPassword.setError(getString(R.string.invalid_pwd_length));
            editOldPassword.requestFocus();
            return false;
        }
        if (newPassword.length() < 6) {
            editNewPassword.setError(getString(R.string.invalid_pwd_length));
            editNewPassword.requestFocus();
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            editConfirmPassword.setError(getString(R.string.invalid_re_password));
            editConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}
