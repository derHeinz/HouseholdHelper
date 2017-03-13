package com.heinz.householdhelper.bt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.heinz.householdhelper.R;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * BT Service that is receiving and storing values from temperature sensors.
 * Created by Heinz on 10.03.2017.
 */
public class BTTempReceiverService extends Service implements MeasurementPopulator{

    /**
     * LOG.
     */
    private static final String TAG = "BTTempReceiverService";

    /**
     * BT Measurement received.
     */
    public static final String BT_MEASUREMENT = "BT_MEASUREMENT";

    /**
     * BT received text.
     */
    public static final String BT_TEXT_KEY = "BT_TEXT_KEY";

    /**
     * BT bluetooth.
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Handler used for async procesing.
     */
    private Handler handler = new Handler();

    /**
     * BT Scanner.
     */
    private ScanningRunnable btScanner;

    private void startBTService() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        Log.d(TAG, "Starting BT service");

        // Ensures Bluetooth is available on the device and it is enabled.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            broadcastText(getString(R.string.bt_error));
            return;
        }

        Log.d(TAG, "Starting BT LE scan");
        FreetecBLECallback callback = new FreetecBLECallback(this);
        btScanner = new ScanningRunnable(handler, mBluetoothAdapter, callback);
        handler.postDelayed(btScanner, 1000);
    }

    private void stopBTService() {
        // only possible started previously
        if (mBluetoothAdapter != null && btScanner != null) {
            Log.d(TAG, "Stopping BT LE scan");
            btScanner.stop();
        }
    }

    @Override
    public void populateMeasurement(FreetecMeasure measure) {
        Intent intent = new Intent(BT_MEASUREMENT);
        intent.putExtra(BT_MEASUREMENT, measure);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastText(String text) {
        Intent intent = new Intent(BT_TEXT_KEY);
        intent.putExtra(BT_TEXT_KEY, text);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startBTService();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopBTService();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
