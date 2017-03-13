package com.heinz.householdhelper.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Heinz on 10.03.2017.
 */

public class FreetecBLECallback implements BluetoothAdapter.LeScanCallback {

    protected static final String TAG = "FreetecBLECallback";

    private MeasurementPopulator measurementPopulator;

    public FreetecBLECallback(MeasurementPopulator btTempServ) {
        measurementPopulator = btTempServ;
    }

    @Override
    public void onLeScan(final BluetoothDevice device, int rssi,
                         byte[] scanRecord) {
        Log.d(TAG, "Received device: " + device.getName());
        if (isFreetecBLEDevice(scanRecord)) {
            Log.d(TAG, "Received real temperature sensor device");
            FreetecMeasure measurement = parsePackage(scanRecord);
            measurement.setAddress(device.getAddress());
            measurement.setTime(new Date());
            Log.d(TAG, "Device infos: " + measurement.toString());
            measurementPopulator.populateMeasurement(measurement);
        }
    }

    protected DecimalFormat temperatureFormat = new DecimalFormat("#0.00");

    protected String decimalTemperatureToStringTemperature(int temp) {
        return temperatureFormat.format((double) temp / 100).replace(",", ".");
    }

    /**
     * Package structure copied from http://stackoverflow.com/questions/25713995/how-to-decode-a-bluetooth-le-package-frame-beacon-of-a-freetec-px-1737-919-b
     02 # Number of bytes that follow in first AD structure
     01 # Flags AD type
     04 # Flags value 0x04 = 000000100
     bit 0 (OFF) LE Limited Discoverable Mode
     bit 1 (OFF) LE General Discoverable Mode
     bit 2 (ON) BR/EDR Not Supported
     bit 3 (OFF) Simultaneous LE and BR/EDR to Same Device Capable (controller)
     bit 4 (OFF) Simultaneous LE and BR/EDR to Same Device Capable (Host)
     09 # Number of bytes that follow in the first AD Structure
     09 # Complete Local Name AD Type
     38 42 42 41 43 34 39 44 # "8BBAC49D"
     07 # Number of bytes that follow in the second AD Structure
     16 # Service Data AD Type
     09 18 # 16-bit Service UUID 0x1809 = Health thermometer (org.bluetooth.service.health_thermometer)
     44 08 00 FE # Additional Service Data 440800  (Temperature = 0x000844 x 10^-2) = 21.16 degrees
     04 # Number of bytes that follow in the third AD Structure
     16 # Service Data AD Type
     0F 18 # 16-bit Service UUID 0x180F  = Battery Service (org.bluetooth.service.battery_service)
     5B # Additional Service Data (battery level)
     B2 # checksum
     * @param scanRecord
     * @return
     */
    protected boolean isFreetecBLEDevice(byte[] scanRecord) {
        if (scanRecord.length != 62) {
            return false;
        }
        if (scanRecord[0] != 2) {
            return false;
        }
        if (scanRecord[1] != 1) {
            return false;
        }
        if (scanRecord[3] != 9) {
            return false;
        }
        if (scanRecord[4] != 9) {
            return false;
        }
        return true;
    }

    protected FreetecMeasure parsePackage(byte[] scanRecord) {
        FreetecMeasure result = new FreetecMeasure();

        byte[] nameBytes = new byte[8];
        System.arraycopy(scanRecord, 5, nameBytes, 0, 8);
        result.setName(new String(nameBytes));

        byte[] tempBytes = new byte[3];
        System.arraycopy(scanRecord, 17, tempBytes, 0, 3);
        int tempInt = 0;
        tempInt += (tempBytes[2] & 0xFF) * 65536;
        tempInt += (tempBytes[1] & 0xFF) * 256;
        tempInt += (tempBytes[0] & 0xFF);
        result.setTemperature(decimalTemperatureToStringTemperature(tempInt));

        result.setBattery(scanRecord[24]);
        return result;
    }
}
