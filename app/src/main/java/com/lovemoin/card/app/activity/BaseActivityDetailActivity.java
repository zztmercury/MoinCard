package com.lovemoin.card.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ActivityInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zzt on 15-11-25.
 */
public class BaseActivityDetailActivity extends BaseActivity {
    protected ActivityInfo activityInfo;

    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), Config.WE_CHAT_APP_ID, true);
        iwxapi.registerApp(Config.WE_CHAT_APP_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (checkShareSupport() && activityInfo.getShareUrl() != null) {
            getMenuInflater().inflate(R.menu.menu_share, menu);
            if (!iwxapi.isWXAppInstalled()) {
                menu.getItem(R.id.action_share_to_we_chat_friend).setVisible(false);
                menu.getItem(R.id.action_share_to_we_chat_timeline).setVisible(false);
            }
            return true;
        } else return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        iwxapi.unregisterApp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share_to_we_chat_friend:
                shareToWeChat(SendMessageToWX.Req.WXSceneSession);
                return true;
            case R.id.action_share_to_we_chat_timeline:
                shareToWeChat(SendMessageToWX.Req.WXSceneTimeline);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareToWeChat(int WXScene) {
        WXWebpageObject object = new WXWebpageObject();
        object.webpageUrl = activityInfo.getShareUrl();

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = object;
        message.title = activityInfo.getName();
        message.description = activityInfo.getBrief();

        app.setCurrentActivityId(activityInfo.getActivityId());

        Bitmap bitmap = null;
        try {
            bitmap = ImageLoader.getInstance().loadImageSync(Config.SERVER_URL + "/moinbox/" + activityInfo.getBriefImg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        bitmap.recycle();
        message.setThumbImage(thumbBitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        req.scene = WXScene;

        iwxapi.sendReq(req);
    }

    private boolean checkShareSupport() {
        return iwxapi.isWXAppInstalled();
    }
}
