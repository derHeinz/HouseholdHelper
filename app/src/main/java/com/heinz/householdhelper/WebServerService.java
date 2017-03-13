package com.heinz.householdhelper;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;
import android.util.Log;

import com.heinz.householdhelper.actions.Action;
import com.heinz.householdhelper.actions.GetBrightnessAction;
import com.heinz.householdhelper.actions.GetTemperatureAction;
import com.heinz.householdhelper.actions.HelpAction;
import com.heinz.householdhelper.actions.PlayAudioAction;
import com.heinz.householdhelper.actions.HasFileAction;
import com.heinz.householdhelper.actions.SpeakAction;
import com.heinz.householdhelper.actions.StoreFileAction;
import com.heinz.householdhelper.actions.ListFilesAction;
import com.heinz.householdhelper.actions.DeleteFileAction;
import com.koushikdutta.async.http.server.AsyncHttpServer;

/**
 * Service to provide web server functionality as long as app lives - even if app is in background.
 * Created by Heinz on 14.10.2016.
 */

public class WebServerService extends Service {

    private static final String TAG = "WebServerService";

    /**
     * WS IP Key.
     */
    public static final String IP_KEY = "WSS_IP_KEY";

    /**
     * WS Text.
     */
    public static final String WS_TEXT = "WSS_TEXT_KY";

    private void startAsynchWebServer() {
        AsyncHttpServer server = new AsyncHttpServer();

        Action[] actions = new Action[]{new PlayAudioAction(), new HasFileAction(), new StoreFileAction(), new ListFilesAction(),
                new DeleteFileAction(), new GetBrightnessAction(), new SpeakAction(), new GetTemperatureAction()};

        for (Action action : actions) {
            action.register(server, this);
            Log.d(TAG, "Registering: " + action);
        }
        // finally append the HelpAction which may inform about every other action.
        new HelpAction(actions).register(server, this);

        // listen on port 5000
        server.listen(5000);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wm == null || wm.getConnectionInfo() == null || wm.getConnectionInfo().getIpAddress() == 0) {
            broadcastText(getString(R.string.ws_error));
        } else {
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            broadcastIP(ip);
        }
    }

    private void broadcastIP(String ip) {
        Intent intent = new Intent(IP_KEY);
        intent.putExtra(IP_KEY, ip);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastText(String text) {
        Intent intent = new Intent(WS_TEXT);
        intent.putExtra(WS_TEXT, text);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAsynchWebServer();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
