package com.heinz.householdhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.heinz.householdhelper.bt.BTTempReceiverService;
import com.heinz.householdhelper.bt.FreetecMeasure;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    /**
     * Services
     */
    private Intent webserver;
    private Intent blueooth;

    /**
     * Text views.
     */
    private TextView mAppText;
    private TextView mIpText;
    private TextView mBtText;

    /**
     * Known BT devices.
     */
    private Map<String, Date> knownBTDevices = new HashMap<>();

    boolean enabled;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void switchState(View view) {
        if (enabled) {
            this.stopService(webserver);
            this.stopService(blueooth);
            // reset known information on UI
            setIPInformationText("-");
            knownBTDevices.clear();
            updateBTInformationText();
        } else {
            this.startService(webserver);
            this.startService(blueooth);
        }
        String msg = enabled?"Server stopped.":"Server started.";
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        enabled = !enabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // bind to aplication text
        mAppText = (TextView) findViewById(R.id.app_text);
        setApplicationText();
        mIpText = (TextView) findViewById(R.id.ip_text);
        mBtText = (TextView) findViewById(R.id.bt_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               switchState(view);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // receive IP and show
                setIPInformationText(intent.getCharSequenceExtra(WebServerService.IP_KEY));
            }
        }, new IntentFilter(WebServerService.IP_KEY));

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // receive device's: show available devices
                FreetecMeasure meas = (FreetecMeasure) intent.getSerializableExtra(BTTempReceiverService.BT_MEASUREMENT);
                knownBTDevices.put(meas.getName(), meas.getTime());
                updateBTInformationText();
            }
        }, new IntentFilter(BTTempReceiverService.BT_MEASUREMENT));

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // receive device's: show available devices
                String text = intent.getStringExtra(BTTempReceiverService.BT_TEXT_KEY);
                setBTInformationText(text);
            }
        }, new IntentFilter(BTTempReceiverService.BT_TEXT_KEY));

        // start service to react under my IP.
        webserver = new Intent(this, WebServerService.class);
        this.startService(webserver);

        // start service to receive bluetooth
        blueooth = new Intent(this, BTTempReceiverService.class);
        this.startService(blueooth);

        enabled = true;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setApplicationText() {
        String s = getString(R.string.text_template);
        String version = getString(R.string.app_version);
        s = s.replace("$version", version);
        mAppText.setText(s);
    }

    private void setIPInformationText(CharSequence ip) {
        String s = getString(R.string.ws_ip_template);
        s = s.replace("$ip", ip);
        mIpText.setText(s);
    }

    private void setBTInformationText(CharSequence text) {
        mBtText.setText(text);
    }

    private void updateBTInformationText() {
        String s = getString(R.string.bt_template);
        List<String> deviceInfos = new ArrayList<>();
        for (String devName: knownBTDevices.keySet()) {
            deviceInfos.add(devName + " updated:" + knownBTDevices.get(devName));
        }
        String devices = TextUtils.join(",",deviceInfos);
        s = s.replace("$devices", devices);
        mBtText.setText(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

