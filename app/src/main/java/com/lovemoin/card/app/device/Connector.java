package com.lovemoin.card.app.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

/**
 * Created by zzt on 15-10-21.
 */
public class Connector {
    private static Connector connector;
    private Context context;
    private NfcAdapter mNfcAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private int connectMode = 0;
    public static final int MODE_BLUETOOTH = 0;
    public static final int MODE_NFC = 1;

    private Connector(Context context) {
        this.context = context.getApplicationContext();

        NfcManager nfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        mNfcAdapter = nfcManager.getDefaultAdapter();
        mBluetoothAdapter = bluetoothManager.getAdapter();

        connectMode = mNfcAdapter == null ? MODE_BLUETOOTH : MODE_NFC;
    }

    public static Connector getInstance(Context context) {
        if (connector == null) {
            connector = new Connector(context);
        }
        return connector;
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
     * @param connectMode 连接模式
     */
    public void setConnectMode(int connectMode) {
        this.connectMode = connectMode;
    }


}
