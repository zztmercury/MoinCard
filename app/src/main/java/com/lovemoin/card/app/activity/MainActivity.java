package com.lovemoin.card.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.lovemoin.card.app.MoinCardApplication;
import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Command;
import com.lovemoin.card.app.db.CardInfo;
import com.lovemoin.card.app.db.CardInfoDao;
import com.lovemoin.card.app.db.DaoMaster;
import com.lovemoin.card.app.db.DaoSession;
import com.lovemoin.card.app.entity.DeviceInfo;
import com.lovemoin.card.app.net.CreateCard;
import com.lovemoin.card.app.net.GetPoint;
import com.lovemoin.card.app.net.QuickExchange;
import com.lovemoin.card.app.utils.CommonUtil;
import de.greenrobot.dao.query.QueryBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-8-24.
 * 程序主入口
 *
 * @author zzt
 */
public class MainActivity extends Activity {
    private MoinCardApplication app;
    private NfcAdapter nfcAdapter;
    private CardInfoDao cardInfoDao;
    private DeviceInfo deviceInfo;
    private Tag tag;
    private IsoDep isoDep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MoinCardApplication) getApplication();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Main", "onResume");
        String action = getIntent().getAction();
        if (action.equals(Intent.ACTION_MAIN)) {
            if (nfcAdapter == null) {
                Toast.makeText(this, R.string.device_do_not_support_nfc, Toast.LENGTH_LONG).show();
            }
            if (app.isLogin() && app.getCachedUserId() != null) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        } else if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            initDao();
            readTag();
        }
    }

    private void initDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "moinCard.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        cardInfoDao = daoSession.getCardInfoDao();
    }

    private void readTag() {
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
                e.printStackTrace();
                finish();
            }
        }
    }

    private void operWait() {
        List<CardInfo> cardList = findCardByMerchantId(deviceInfo.getMerchantCode());
        if (cardList.size() > 1) {
            Intent i = new Intent(this, CardSelectorActivity.class);
            i.putExtra(CardSelectorActivity.CARD_LIST, (Serializable) cardList);
            startActivity(i);
        } else {
            if (cardList.size() == 1) {
                app.setCurrentCard(cardList.get(0));
                startActivity(new Intent(this, MerchantDetailActivity.class));
            } else {
                createCard(deviceInfo.getMerchantCode(), false);
            }
        }
    }

    private void operPlus() {
        CardInfo cardInfo = findPointCardByMerchantId(deviceInfo.getMerchantCode());
        if (cardInfo == null) {
            createCard(deviceInfo.getMerchantCode(), true);
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
                                app.setIsExchange(false);
                                app.getCurrentCard().setCurrentPoint(app.getCurrentCard().getCurrentPoint() - app.getCurrentCard().getConvertPoint());
                                cardInfoDao.insertOrReplace(app.getCurrentCard());
                                Toast.makeText(MainActivity.this, "兑换成功", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(MainActivity.this, ConvertSuccessActivity.class);
                                startActivity(i);
                            }

                            @Override
                            public void onFail(String message) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        };
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, R.string.card_not_satisfy, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.please_press_card_first, Toast.LENGTH_LONG).show();
        }
    }

    private void operSign() {

    }

    private void operCardConfirm() {

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


    private void createCard(String merchantId, final boolean isPlus) {
        new CreateCard(app.getCachedUserId(), merchantId) {
            @Override
            public void onSuccess(CardInfo cardInfo) {
                cardInfoDao.insert(cardInfo);
                if (isPlus) {
                    app.setCurrentCard(cardInfo);
                    plusCommand();
                } else {
                    app.setCurrentCard(cardInfo);
                    startActivity(new Intent(MainActivity.this, MerchantDetailActivity.class));
                }
            }

            @Override
            public void onFail(String message) {

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

                        Intent i = new Intent(MainActivity.this, CardSelectorActivity.class);
                        List<CardInfo> cardList = new ArrayList<>();
                        cardList.add(app.getCurrentCard());
                        i.putExtra(CardSelectorActivity.CARD_LIST, (Serializable) cardList);
                        i.putExtra(CardSelectorActivity.COUNT, deviceInfo.getPoint());
                        startActivity(i);
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                };
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
