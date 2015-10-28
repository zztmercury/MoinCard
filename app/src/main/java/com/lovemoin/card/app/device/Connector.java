package com.lovemoin.card.app.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

import com.lovemoin.card.app.utils.CommonUtil;

import java.util.UUID;

/**
 * Created by zzt on 15-10-21.
 * 准备将NFC和蓝牙封装在一起，暂未找到合适的解决方式
 */
public class Connector {
    public static final int MODE_BLUETOOTH = 0;
    public static final int MODE_NFC = 1;
    private static final String SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_UUID = "0000fff6-0000-1000-8000-00805f9b34fb";
    private static Connector connector;
    private Context context;
    private NfcAdapter mNfcAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private int connectMode = 0;
    private CallBack callBack;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mGattCharacteristic;

    private Connector(Context context) {
        this.context = context.getApplicationContext();

        NfcManager nfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        mNfcAdapter = nfcManager.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();

        callBack = new CallBack() {
            @Override
            public void onReceive(String message) {

            }

            @Override
            public void onDevicesDiscovered() {

            }
        };

        setConnectMode(MODE_NFC);
    }

    public static Connector getInstance(Context context) {
        if (connector == null) {
            connector = new Connector(context);
        }
        return connector;
    }

    public void send(int id, String command) {
        byte[] bytes = CommonUtil.HexStringToByteArray(command);
        switch (connectMode) {
            case MODE_NFC:
                //byte[] result =
                break;
            case MODE_BLUETOOTH:

                break;
        }
    }


    public int getConnectMode() {
        return connectMode;
    }

    /**
     * 设置与设备通信的方式
     * <p>
     * 当前可用参数有:
     * <dl>
     * <dd>{@link #MODE_BLUETOOTH} 蓝牙</dd>
     * <dd>{@link #MODE_NFC} NFC</dd>
     * </dl>
     * </p>
     *
     * @param connectMode 连接模式
     */
    public void setConnectMode(int connectMode) {
        if (mNfcAdapter == null)
            this.connectMode = MODE_BLUETOOTH;
        else this.connectMode = connectMode;
        switch (this.connectMode) {
            case MODE_BLUETOOTH:
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(enableBtIntent);
                }
                if (mBluetoothAdapter.isEnabled()) {
                    leScan();
                }
                break;
            case MODE_NFC:

                break;
        }
    }

    private void leScan() {
        mBluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if ("MO-IN BOX".equals(device.getName())) {
                    if (rssi >= -75 && mBluetoothGatt == null) {
                        mBluetoothGatt = device.connectGatt(context, false, new BluetoothGattCallback() {
                            @Override
                            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                                super.onConnectionStateChange(gatt, status, newState);
                                if (newState == BluetoothAdapter.STATE_CONNECTED) {
                                    gatt.discoverServices();
                                }
                            }

                            @Override
                            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                                super.onServicesDiscovered(gatt, status);
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    BluetoothGattService gattService = gatt.getService(UUID.fromString(SERVICE_UUID));
                                    if (gattService != null) {
                                        BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID));
                                        if (gattCharacteristic != null) {
                                            gatt.setCharacteristicNotification(gattCharacteristic, true);
                                            mGattCharacteristic = gattCharacteristic;
                                            callBack.onDevicesDiscovered();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                                super.onCharacteristicChanged(gatt, characteristic);
                            }
                        });
                    } else if (rssi >= -75 && mBluetoothGatt != null) {
                        mBluetoothGatt.connect();
                    }
                }
            }
        });
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public interface CallBack {
        void onReceive(String message);

        void onDevicesDiscovered();
    }

}
