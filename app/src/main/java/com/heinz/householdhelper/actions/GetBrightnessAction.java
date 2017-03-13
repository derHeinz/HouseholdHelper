package com.heinz.householdhelper.actions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Heinz on 18.10.2016.
 * Returns the current temperature in
 */

public class GetBrightnessAction extends AbstractAsyncTaskAction {

    private static final String TAG = "GetBrightnessAction";

    @Override
    public String getResourcePrefix() {
        return "/getBrightness";
    }

    @Override
    public HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.GET};
    }

    @Override
    public String getDescription() {
        return "Returns the current light level in Lux.";
    }

    // the light sensor used to determine the light level.
    SensorEventListener lightListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            finisher.finished(event.values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // nothing to do here.
        }
    };

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // light-sensor
        finisher = new Finisher<Float>();
        sm.registerListener(lightListener, sm.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI);
        waitForFinisher();
        sm.unregisterListener(lightListener);

        JSONObject result = new JSONObject();
        try {
            result.put("light", finisher.getResult());
            response.send(result);
        } catch (JSONException e) {
            Log.e(TAG, "Error sending JSON", e);
            response.code(400);
            response.end();
        }

    }
}
