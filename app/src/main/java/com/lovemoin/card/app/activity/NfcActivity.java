package com.lovemoin.card.app.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.Toast;

import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Command;
import com.lovemoin.card.app.constant.ResultCode;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.entity.DeviceInfo;
import com.lovemoin.card.app.net.CreateCard;
import com.lovemoin.card.app.net.GetPoint;
import com.lovemoin.card.app.net.QuickExchange;
import com.lovemoin.card.app.net.SignIn;
import com.lovemoin.card.app.utils.CommonUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zzt on 15-8-24.
 * 程序主入口
 *
 * @author zzt
 */
public class NfcActivity extends Activity {
    private MoinCardApplication app;
    private CardInfoDao cardInfoDao;
    private DeviceInfo deviceInfo;
    private Tag tag;
    private IsoDep isoDep;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private NfcAdapter mAdapter;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MoinCardApplication) getApplication();
        if (app.getCachedUserId() == null || app.getCachedUserTel() == null) {
            app.reset();
            Toast.makeText(getApplicationContext(), R.string.error_login_before_convert, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        cardInfoDao = app.getCardInfoDao();
        pd = new ProgressDialog(this);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcForegroundDispatch();
        readTag();
    }


    private void nfcForegroundDispatch() {

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readTag();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                    mTechLists);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.disableForegroundDispatch(this);
    }

    private void readTag() {
        pd.show();
        tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();
                byte[] result;
                byte[] command;
                String commandStr;
                commandStr = Command.COMMAND_DEVICE_STAT;
                command = CommonUtil.HexStringToByteArray(commandStr);
                result = isoDep.transceive(command);
                deviceInfo = new DeviceInfo(CommonUtil.ByteArrayToHexString(result));
                switch (deviceInfo.getOper()) {
                    case DeviceInfo.OPER_WAITE:
                        operWait();
                        break;
                    case DeviceInfo.OPER_PLUS:
                        operPlus();
                        break;
                    case DeviceInfo.OPER_CONVERT:
                        operConvert();
                        break;
                    case DeviceInfo.OPER_SIGN:
                        operSign();
                        break;
                    case DeviceInfo.OPER_CARD_CONFIRM:
                        operCardConfirm();
                        break;
                    default:
                }
            } catch (IOException e) {
                pd.dismiss();
                e.printStackTrace();
                finish();
            }
        }
        finish();
        pd.dismiss();
    }

    private void operWait() {
        List<CardInfo> cardList = findCardByMerchantId(deviceInfo.getMerchantCode());
        if (cardList.size() > 1) {
            pd.dismiss();
            Intent i = new Intent(this, CardSelectorActivity.class);
            i.putExtra(CardSelectorActivity.CARD_LIST, (Serializable) cardList);
            startActivity(i);
            finish();
        } else {
            if (cardList.size() == 1) {
                pd.dismiss();
                app.setCurrentCard(cardList.get(0));
                startActivity(new Intent(this, MerchantDetailActivity.class));
                finish();
            } else {
                createCard(deviceInfo.getMerchantCode(), CardInfo.TYPE_POINT, false);
            }
        }
    }

    private void operPlus() {
        CardInfo cardInfo = findPointCardByMerchantId(deviceInfo.getMerchantCode());
        if (cardInfo == null) {
            createCard(deviceInfo.getMerchantCode(), CardInfo.TYPE_POINT, true);
        } else {
            app.setCurrentCard(cardInfo);
            plusCommand();
        }
    }

    private void operConvert() {
        if (app.isExchange()) {
            if (app.getCurrentCard().getCardCode().startsWith(deviceInfo.getMerchantCode())) {
                try {
                    byte[] result;
                    byte[] command;
                    String commandStr;
                    commandStr = Command.COMMAND_DEVICE_CONVERT + app.getCurrentCard().getCardCode() + CommonUtil.padLeft(Integer.toHexString(app.getCurrentCard().getCurrentPoint()), 4);
                    command = CommonUtil.HexStringToByteArray(commandStr);
                    result = isoDep.transceive(command);
                    String checkValue = CommonUtil.ByteArrayToHexString(result);
                    if (checkValue.endsWith(Command.SUCCEED_END_STR)) {
                        checkValue = checkValue.substring(0, checkValue.lastIndexOf(Command.SUCCEED_END_STR));
                        new QuickExchange(checkValue, app.getCachedUserId()) {
                            @Override
                            public void onSuccess() {
                                setResult(ResultCode.EXCHANGE_SUCCESS);
                                pd.dismiss();
                                app.setIsExchange(false);
                                app.getCurrentCard().setCurrentPoint(app.getCurrentCard().getCurrentPoint() - app.getCurrentCard().getConvertPoint());
                                cardInfoDao.insertOrReplace(app.getCurrentCard());
                                Toast.makeText(NfcActivity.this, "兑换成功", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(NfcActivity.this, ConvertSuccessActivity.class);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onFail(String message) {
                                Toast.makeText(NfcActivity.this, message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                                finish();
                            }
                        };
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    pd.dismiss();
                    finish();
                }
            } else {
                Toast.makeText(this, R.string.card_not_satisfy, Toast.LENGTH_LONG).show();
                pd.dismiss();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.please_press_card_first, Toast.LENGTH_LONG).show();
            pd.dismiss();
            finish();
        }
    }

    private void operSign() {
        CardInfo cardInfo = findSignCardByMerchantId(deviceInfo.getMerchantCode());
        if (cardInfo == null) {
            createCard(deviceInfo.getMerchantCode(), CardInfo.TYPE_SIGN, true);
        } else {
            app.setCurrentCard(cardInfo);
            signCommand();
        }
    }

    private void operCardConfirm() {
        pd.dismiss();
        finish();
    }

    /**
     * 根据merchantId获得对应的积点卡
     *
     * @param merchantCode 可通过DeviceInfo.getMerchantCode()得到
     * @return 用户拥有的该商户下所有的卡
     */
    private List<CardInfo> findCardByMerchantId(String merchantCode) {
        QueryBuilder<CardInfo> queryBuilder = cardInfoDao.queryBuilder();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        queryBuilder.where(CardInfoDao.Properties.CardCode.like(merchantCode + "%"));
        return queryBuilder.list();
    }

    private CardInfo findPointCardByMerchantId(String merchantId) {
        QueryBuilder<CardInfo> queryBuilder = cardInfoDao.queryBuilder();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        queryBuilder.where(CardInfoDao.Properties.CardCode.like(merchantId + "%"), CardInfoDao.Properties.CardType.eq(CardInfo.TYPE_POINT));
        return queryBuilder.unique();
    }

    private CardInfo findSignCardByMerchantId(String merchantId) {
        QueryBuilder<CardInfo> queryBuilder = cardInfoDao.queryBuilder();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        queryBuilder.where(CardInfoDao.Properties.CardCode.like(merchantId + "%"), CardInfoDao.Properties.CardType.eq(CardInfo.TYPE_SIGN));
        return queryBuilder.unique();
    }


    private void createCard(final String merchantId, final String cardType, final boolean isPlus) {
        new CreateCard(app.getCachedUserId(), merchantId, cardType) {
            @Override
            public void onSuccess(CardInfo cardInfo) {
                cardInfoDao.insert(cardInfo);
                if (isPlus && cardType.equals(CardInfo.TYPE_POINT)) {
                    app.setCurrentCard(cardInfo);
                    plusCommand();
                } else if (isPlus && cardType.equals(CardInfo.TYPE_SIGN)) {
                    app.setCurrentCard(cardInfo);
                    signCommand();
                } else {
                    pd.dismiss();
                    app.setCurrentCard(cardInfo);
                    startActivity(new Intent(NfcActivity.this, MerchantDetailActivity.class));
                    finish();
                }
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(NfcActivity.this, message, Toast.LENGTH_LONG).show();
                pd.dismiss();
                finish();
            }
        };
    }

    private void plusCommand() {
        try {
            byte[] result;
            byte[] command;
            String commandStr;
            commandStr = Command.COMMAND_DEVICE_PLUS + app.getCurrentCard().getCardCode();
            command = CommonUtil.HexStringToByteArray(commandStr);
            result = isoDep.transceive(command);
            String checkValue = CommonUtil.ByteArrayToHexString(result);
            if (checkValue.endsWith(Command.SUCCEED_END_STR)) {
                checkValue = checkValue.substring(0, checkValue.lastIndexOf(Command.SUCCEED_END_STR));
                new GetPoint(checkValue, app.getCachedUserId()) {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Intent i = new Intent(NfcActivity.this, CardSelectorActivity.class);
                        List<CardInfo> cardList = new ArrayList<>();
                        cardList.add(app.getCurrentCard());
                        i.putExtra(CardSelectorActivity.CARD_LIST, (Serializable) cardList);
                        i.putExtra(CardSelectorActivity.COUNT, deviceInfo.getPoint());
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        pd.dismiss();
                        Toast.makeText(NfcActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                };
            }

        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void signCommand() {
        try {
            byte[] result;
            byte[] command;
            String commandStr;
            commandStr = Command.COMMAND_DEVICE_SIGN_IN + app.getCurrentCard().getCardCode();
            command = CommonUtil.HexStringToByteArray(commandStr);
            result = isoDep.transceive(command);
            String checkValue = CommonUtil.ByteArrayToHexString(result);
            if (checkValue.endsWith(Command.SUCCEED_END_STR)) {
                checkValue = checkValue.substring(0, checkValue.lastIndexOf(Command.SUCCEED_END_STR));
                new SignIn(checkValue, app.getCachedUserId()) {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        Intent i = new Intent(NfcActivity.this, CardSelectorActivity.class);
                        List<CardInfo> cardList = new ArrayList<>();
                        app.getCurrentCard().setCurrentPoint(app.getCurrentCard().getCurrentPoint() + 1);
                        cardList.add(app.getCurrentCard());
                        i.putExtra(CardSelectorActivity.CARD_LIST, (Serializable) cardList);
                        i.putExtra(CardSelectorActivity.COUNT, -2);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        pd.dismiss();
                        Toast.makeText(NfcActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                };
            }

        } catch (IOException e) {
            finish();
            e.printStackTrace();
            finish();
        }
    }

}
