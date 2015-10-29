package com.lovemoin.card.app.activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by zzt on 15-9-22.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_UUID = "0000fff6-0000-1000-8000-00805f9b34fb";
    protected MoinCardApplication app;
    protected ProgressDialog pd;
    protected NfcAdapter nfcAdapter;
    protected BluetoothAdapter mBluetoothAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mGattCharacteristic;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private static void delay(int ms) {
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        app = (MoinCardApplication) getApplication();
        NfcManager nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        nfcAdapter = nfcManager.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled() && app.isPromptNfc()) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.hint_enable_nfc)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.ignore, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                app.setPromptNfc(false);
                                app.setConnectMode(MoinCardApplication.MODE_BLUETOOTH);
                                mBluetoothAdapter.enable();
                                leScan();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
            nfcForegroundDispatch();
        } else {
            app.setConnectMode(MoinCardApplication.MODE_BLUETOOTH);
            if (!mBluetoothAdapter.isEnabled()) {
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(i);
            }
        }
        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            private String revStr = "";
            private DeviceInfo deviceInfo;

            private void operPlus() {
                CardInfo cardInfo = findPointCardByMerchantId(deviceInfo.getMerchantCode());
                if (cardInfo == null) {
                    createCard(deviceInfo.getMerchantCode(), CardInfo.TYPE_POINT, true);
                } else {
                    app.setCurrentCard(cardInfo);
                    writeByBluetooth(Command.SHORT_COMMAND_DEVICE_PLUS + app.getCurrentCard().getCardCode().substring(12));
                }
            }

            private void operConvert() {
                if (app.isExchange()) {
                    if (app.getCurrentCard().getCardCode().startsWith(deviceInfo.getMerchantCode())) {
                        String commandStr = Command.SHORT_COMMAND_DEVICE_CONVERT + app.getCurrentCard().getCardCode().substring(12) + CommonUtil.padLeft(Integer.toHexString(app.getCurrentCard().getCurrentPoint()), 4);
                        writeByBluetooth(commandStr);
                    }
                }
            }

            private void operSign() {
                CardInfo cardInfo = findSignCardByMerchantId(deviceInfo.getMerchantCode());
                if (cardInfo == null) {
                    createCard(deviceInfo.getMerchantCode(), CardInfo.TYPE_SIGN, true);
                } else {
                    app.setCurrentCard(cardInfo);
                    String commandStr = Command.COMMAND_DEVICE_SIGN_IN + app.getCurrentCard().getCardCode();
                    writeByBluetooth(commandStr);
                }
            }

            private void createCard(final String merchantId, final String cardType, final boolean isPlus) {
                new CreateCard(app.getCachedUserId(), merchantId, cardType) {
                    @Override
                    public void onSuccess(CardInfo cardInfo) {
                        app.getCardInfoDao().insert(cardInfo);
                        if (isPlus && cardType.equals(CardInfo.TYPE_POINT)) {
                            app.setCurrentCard(cardInfo);
                            writeByBluetooth(Command.COMMAND_DEVICE_PLUS + app.getCurrentCard().getCardCode());
                        } else if (isPlus && cardType.equals(CardInfo.TYPE_SIGN)) {
                            app.setCurrentCard(cardInfo);
                            writeByBluetooth(Command.COMMAND_DEVICE_PLUS + app.getCurrentCard().getCardCode());
                        } else {
                            pd.dismiss();
                            app.setCurrentCard(cardInfo);
                            startActivity(new Intent(BaseActivity.this, MerchantDetailActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        pd.dismiss();
                        finish();
                    }
                };
            }

            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if ("MO-IN BOX".equals(device.getName())) {
                    pd.setMessage(getString(R.string.connecting_device));
                    if (rssi >= -70 && mBluetoothGatt == null) {
                        pd.show();
                        mBluetoothGatt = device.connectGatt(getApplicationContext(), false, new BluetoothGattCallback() {
                            @Override
                            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                                super.onConnectionStateChange(gatt, status, newState);
                                if (newState == BluetoothAdapter.STATE_CONNECTED) {
                                    deviceInfo = null;
                                    revStr = "";
                                    gatt.discoverServices();
                                }
                            }

                            @Override
                            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                                super.onServicesDiscovered(gatt, status);
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    for (BluetoothGattService gattService : gatt.getServices()) {
                                        if (gattService.getUuid().toString().equals(SERVICE_UUID)) {
                                            for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
                                                if (gattCharacteristic.getUuid().toString().equals(CHARACTERISTIC_UUID)) {
                                                    mBluetoothAdapter.stopLeScan(null);
                                                    gatt.setCharacteristicNotification(gattCharacteristic, true);
                                                    mGattCharacteristic = gattCharacteristic;
                                                    writeByBluetooth(Command.COMMAND_DEVICE_STAT);

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                                super.onCharacteristicChanged(gatt, characteristic);
                                byte[] data = characteristic.getValue();
                                String hexStr = CommonUtil.ByteArrayToHexString(data);
                                revStr += hexStr.substring(2, hexStr.length() - 2);
                                if (data[0] == 0) {
                                    if (revStr.endsWith(Command.SUCCEED_END_STR)) {
                                        revStr = revStr.substring(0, revStr.lastIndexOf(Command.SUCCEED_END_STR));
                                        switch (app.stat) {
                                            case DeviceInfo.OPER_WAITE:
                                                deviceInfo = new DeviceInfo(revStr);
                                                revStr = "";
                                                app.stat = deviceInfo.getOper();
                                                switch (app.stat) {
                                                    case DeviceInfo.OPER_WAITE:
                                                        writeByBluetooth(Command.COMMAND_DEVICE_STAT);
                                                        break;
                                                    case DeviceInfo.OPER_PLUS:
                                                        operPlus();
                                                        break;
                                                    case DeviceInfo.OPER_CONVERT:
                                                        operConvert();
                                                        //writeByBluetooth(Command.COMMAND_DEVICE_CONVERT);
                                                        break;
                                                    case DeviceInfo.OPER_SIGN:
                                                        operSign();
                                                        break;
                                                }
                                                break;
                                            case DeviceInfo.OPER_PLUS:
                                                new GetPoint(revStr, app.getCachedUserId()) {
                                                    @Override
                                                    public void onSuccess() {
                                                        app.stat = DeviceInfo.OPER_WAITE;
                                                        pd.dismiss();
                                                        Intent i = new Intent(BaseActivity.this, CardSelectorActivity.class);
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
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                };
                                                break;
                                            case DeviceInfo.OPER_CONVERT:
                                                new QuickExchange(revStr, app.getCachedUserId()) {

                                                    @Override
                                                    public void onSuccess() {
                                                        app.setIsExchange(false);
                                                        app.stat = DeviceInfo.OPER_WAITE;
                                                        pd.dismiss();
                                                        app.setIsExchange(false);
                                                        app.getCurrentCard().setCurrentPoint(app.getCurrentCard().getCurrentPoint() - app.getCurrentCard().getConvertPoint());
                                                        app.getCardInfoDao().insertOrReplace(app.getCurrentCard());
                                                        Toast.makeText(getApplicationContext(), "兑换成功", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(BaseActivity.this, ConvertSuccessActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFail(String message) {
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                                        pd.dismiss();
                                                        finish();
                                                    }
                                                };
                                                break;
                                            case DeviceInfo.OPER_SIGN:
                                                new SignIn(revStr, app.getCachedUserId()) {
                                                    @Override
                                                    public void onSuccess() {
                                                        pd.dismiss();
                                                        Intent i = new Intent(BaseActivity.this, CardSelectorActivity.class);
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
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                };
                                                break;
                                        }
                                    }
                                }
                            }
                        });
                    } else if (rssi >= -70 && mBluetoothGatt != null) {
                        mBluetoothGatt.connect();
                        pd.show();
                    }
                }
            }
        };
    }

    private void leScan() {
        UUID[] uuids = {UUID.fromString(SERVICE_UUID)};
        mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
    }

    private void writeByBluetooth(String s) {
        int sendlen = s.length() / 2;
        int sendoffset = 0;
        byte[] sendbuffer = CommonUtil.HexStringToByteArray(s);
        while (sendlen > 0) {
            delay(300);
            if (sendlen > 17) {
                byte[] tempbuffer = new byte[19];
                tempbuffer[0] = 0X40;
                System.arraycopy(sendbuffer, sendoffset, tempbuffer, 1, 17);

                tempbuffer[18] = CommonUtil.xor(tempbuffer, 18);
                sendlen = sendlen - 17;
                sendoffset = (byte) (sendoffset + 17);
                mGattCharacteristic.setValue(tempbuffer);
                mBluetoothGatt.writeCharacteristic(mGattCharacteristic);
            } else {
                byte[] tempbuffer = new byte[sendlen + 2];
                tempbuffer[0] = 0X00;
                System.arraycopy(sendbuffer, sendoffset, tempbuffer, 1, sendlen);
                tempbuffer[sendlen + 1] = CommonUtil.xor(tempbuffer, sendlen + 1);
                mGattCharacteristic.setValue(tempbuffer);
                mBluetoothGatt.writeCharacteristic(mGattCharacteristic);
                break;
            }

        }
    }

    private CardInfo findPointCardByMerchantId(String merchantId) {
        QueryBuilder<CardInfo> queryBuilder = app.getCardInfoDao().queryBuilder();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        queryBuilder.where(CardInfoDao.Properties.CardCode.like(merchantId + "%"), CardInfoDao.Properties.CardType.eq(CardInfo.TYPE_POINT));
        return queryBuilder.unique();
    }

    private CardInfo findSignCardByMerchantId(String merchantId) {
        QueryBuilder<CardInfo> queryBuilder = app.getCardInfoDao().queryBuilder();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        queryBuilder.where(CardInfoDao.Properties.CardCode.like(merchantId + "%"), CardInfoDao.Properties.CardType.eq(CardInfo.TYPE_SIGN));
        return queryBuilder.unique();
    }

    private void nfcForegroundDispatch() {
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
            case 100:
                if (resultCode == ResultCode.EXCHANGE_SUCCESS) {
                    pd.dismiss();
                    break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        switch (app.getConnectMode()) {
            case MoinCardApplication.MODE_BLUETOOTH:
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (app.getConnectMode() == MoinCardApplication.MODE_NFC) {
            String action = intent.getAction();
            Log.d("action", action == null ? "null" : action);
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
                intent.setClass(this, NfcActivity.class);
                if (!app.isExchange()) {
                    startActivity(intent);
                } else
                    startActivityForResult(intent, 100);
            } else {
                setIntent(intent);
            }
        }
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
            if (nfcAdapter.isEnabled())
                app.setConnectMode(MoinCardApplication.MODE_NFC);
        }

        switch (app.getConnectMode()) {
            case MoinCardApplication.MODE_BLUETOOTH:
                leScan();
                break;
            case MoinCardApplication.MODE_NFC:

                break;
        }
    }
}
