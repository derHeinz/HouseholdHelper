package com.heinz.householdhelper.bt;

import android.os.Handler;
import android.bluetooth.BluetoothAdapter;

/**
 * Created by Heinz on 11.03.2017.
 */

public class ScanningRunnable implements Runnable {

    private boolean state;

    private BluetoothAdapter btAdapter;

    private BluetoothAdapter.LeScanCallback btCallback;

    private Handler handler;

    public ScanningRunnable(Handler handl, BluetoothAdapter adapter, BluetoothAdapter.LeScanCallback callback) {
        handler = handl;
        btAdapter = adapter;
        btCallback = callback;
    }

    public boolean getState() {
        return state;
    }

    protected void stop() {
        btAdapter.stopLeScan(btCallback);
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        long timeInterval;
        if (!state) {
            btAdapter.startLeScan(btCallback);
            // interval for scanning
            timeInterval = 1000 * 20;
        } else {
            btAdapter.stopLeScan(btCallback);
            // offline interval
            timeInterval = 1000 * 50 * 5;

        }
        handler.postDelayed(this, timeInterval);
        state = !state;
    }
}
