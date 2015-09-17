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
import com.lovemoin.card.app.utils.CommonUtil;
import de.greenrobot.dao.query.QueryBuilder;

import java.io.IOException;
import java.io.Serializable;
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
        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        IsoDep isoDep = IsoDep.get(tag);
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
            }
        }
    }

    private void operWait() {
        List<CardInfo> cardList = findByMerchantId(deviceInfo.getMerchantId());
        if (cardList.size() > 1) {
            Intent i = new Intent(this, CardSelectorActivity.class);
            i.putExtra(CardSelectorActivity.CARD_LIST, (Serializable) cardList);
            startActivity(i);
        } else {
            if (cardList.size() == 1) {
                app.setCurrentCard(cardList.get(0));
                startActivity(new Intent(this, MerchantDetailActivity.class));
            } else {
                createCard(deviceInfo.getMerchantId());
            }
        }
    }

    private void operPlus() {

    }

    private void operConvert() {

    }

    private void operSign() {

    }

    private void operCardConfirm() {

    }

    /**
     * 根据merchantId获得对应的积点卡
     *
     * @param merchantId 后台配置的Id，可通过DeviceInfo.getMerchantId()得到
     * @return 用户拥有的该商户下所有的积点卡
     */
    private List<CardInfo> findByMerchantId(String merchantId) {
        QueryBuilder<CardInfo> queryBuilder = cardInfoDao.queryBuilder();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        queryBuilder.where(CardInfoDao.Properties.CardCode.like(merchantId + "%"), CardInfoDao.Properties.CardType.eq(CardInfo.TYPE_POINT));
        return queryBuilder.list();
    }

    private void createCard(String merchantId) {
        new CreateCard(app.getCachedUserId(), merchantId) {
            @Override
            public void onSuccess(CardInfo cardInfo) {
                app.setCurrentCard(cardInfo);
                startActivity(new Intent(MainActivity.this, MerchantDetailActivity.class));
            }

            @Override
            public void onFail(String message) {

            }
        };
    }
}
