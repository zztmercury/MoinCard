package com.lovemoin.card.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.activity.EntranceActivity;
import com.lovemoin.card.app.activity.HomeActivity;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.constant.ResultCode;
import com.lovemoin.card.app.net.LoginByWeChat;
import com.lovemoin.card.app.net.SendShareLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zzt on 15-11-16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;
    private MoinCardApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MoinCardApplication) getApplication();
        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WE_CHAT_APP_ID, true);
        iwxapi.registerApp(Config.WE_CHAT_APP_ID);
        iwxapi.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        app = (MoinCardApplication) getApplication();
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            String versionName = app.getVersionName();

            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            new LoginByWeChat(resp.code, versionName, deviceId) {
                @Override
                protected void onSuccess(String userId, String userTel, String userImg, String username) {
                    app.cachedUserId(userId);
                    app.cacheUserTel(userTel);
                    app.cacheUserImg(userImg);
                    app.cacheUsername(username);
                    app.cacheLoginStatus(true);
                    startActivity(new Intent(WXEntryActivity.this, HomeActivity.class));
                    setResult(ResultCode.LOGIN_SUCCESS);
                    finish();
                }

                @Override
                protected void onFail(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WXEntryActivity.this, EntranceActivity.class));
                    finish();
                }
            };
        } else if (baseResp instanceof SendMessageToWX.Resp) {
            SendMessageToWX.Resp resp = (SendMessageToWX.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if (!TextUtils.isEmpty(app.getCurrentActivityId()))
                        new SendShareLog(app.getCachedUserId(), app.getCurrentActivityId()) {

                            @Override
                            protected void onFinish() {
                                finish();
                            }
                        };
                    break;

                default:
                    finish();
            }
        }
    }
}
