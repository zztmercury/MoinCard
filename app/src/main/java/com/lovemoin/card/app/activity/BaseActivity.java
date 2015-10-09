package com.lovemoin.card.app.activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.lovemoin.card.app.MoinCardApplication;

/**
 * Created by zzt on 15-9-22.
 */
public class BaseActivity extends AppCompatActivity {
    public static final int EXCHANGE_SUCCESS = RESULT_FIRST_USER + 1;
    protected MoinCardApplication app;
    protected ProgressDialog pd;
    protected NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        app = (MoinCardApplication) getApplication();
        nfcForegroundDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Log.d("action", action == null ? "null" : action);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            intent.setClass(this, NfcActivity.class);
            if (!app.isExchange()) {
                startActivity(intent);
            } else
                startActivityForResult(intent, EXCHANGE_SUCCESS);
        } else {
            setIntent(intent);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    private void nfcForegroundDispatch() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup an intent filter for all MIME based dispatches
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        tech.addCategory(Intent.CATEGORY_DEFAULT);
        // try {
        // tech.addDataType("*/*");
        // } catch (MalformedMimeTypeException e) {
        // throw new RuntimeException("fail", e);
        // }
        mFilters = new IntentFilter[]{tech};

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][]{
                new String[]{MifareClassic.class.getName()},
                new String[]{NfcA.class.getName()}};// 允许扫描的标签类型

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EXCHANGE_SUCCESS:
                pd.dismiss();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
