package com.heinz.householdhelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.heinz.householdhelper.actions.Action;
import com.heinz.householdhelper.actions.PlayAudioAction;
import com.heinz.householdhelper.actions.HasFileAction;
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

    private void startAsynchWebServer() {
        AsyncHttpServer server = new AsyncHttpServer();

        Action[] actions = new Action[]{new PlayAudioAction(), new HasFileAction(), new StoreFileAction(), new ListFilesAction(), new DeleteFileAction()};

        for (Action action : actions) {
            action.register(server, this);
            Log.d(TAG, "Registering: " + action);
        }

        // listen on port 5000
        server.listen(5000);
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

