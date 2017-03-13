package com.heinz.householdhelper.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.heinz.householdhelper.HttpMethod;
import com.heinz.householdhelper.bt.BTTempReceiverService;
import com.heinz.householdhelper.bt.FreetecMeasure;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Heinz on 11.03.2017.
 */

public class GetTemperatureAction extends AbstractAction {

    private static final String TAG = "GetTemperatureAction";

    private static final String MAC_PATTERN_STR = "([0-9A-F]{2}:){5}[0-9A-F]{2}";

    private static final Pattern MAC_PATTERN = Pattern.compile(MAC_PATTERN_STR);

    /**
     * Stores device mac to measurement.
     */
    Map<String, FreetecMeasure> measures = new HashMap<>();

    @Override
    public void register(AsyncHttpServer server, Context ctx) {
        super.register(server, ctx);
        // init receveiving of callback
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FreetecMeasure meas = (FreetecMeasure) intent.getSerializableExtra(BTTempReceiverService.BT_MEASUREMENT);
                measures.put(meas.getAddress(), meas);
            }
        }, new IntentFilter(BTTempReceiverService.BT_MEASUREMENT));

    }

    @Override
    public HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.GET};
    }

    @Override
    public String getResourcePrefix() {
        return "/getTemperature/";
    }

    @Override
    public String getResourceAppend() {
        return "[A-Z,0-9,%]+";
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
        String decoded = null;
        // unparseable content
        try {
            decoded = URLDecoder.decode(restOfPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error receiving: " + restOfPath);
            response.code(500);
            response.end();
            return;
        }

        // not an actual valid mac address
        if (!checkRealMac(decoded)) {
            response.code(400);
            response.end();
            return;
        }

        // lookup result
        FreetecMeasure measure = measures.get(decoded);

        if (measure == null) {
            response.code(404);
            response.end();
            return;
        }

        JSONObject result = new JSONObject();
        try {
            result.put("name", measure.getName());
            result.put("battery", measure.getBattery());
            result.put("temperature", measure.getTemperature());
            result.put("time", toISO8601LocalDate(measure.getTime()));
            response.send(result);
            response.code(200);
        } catch (JSONException e) {
            Log.e(TAG, "Error sending JSON", e);
            response.code(400);
        }
        response.end();
    }

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    protected String toISO8601LocalDate(Date date) {
        return dateFormat.format(date);
    }

    protected boolean checkRealMac(String mac) {
        return MAC_PATTERN.matcher(mac).matches();
    }

    @Override
    public String getDescription() {
        return "Returns the temperature and metadata of a given device.";
    }


}
