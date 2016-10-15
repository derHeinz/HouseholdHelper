package com.heinz.householdhelper.actions;

import android.media.MediaPlayer;
import android.net.Uri;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.io.File;

/**
 * Perfom to play an audio file.
 * use actions like /playAudio/audiofilename
 * Created by Heinz on 14.10.2016.
 */

public class PlayAudioAction extends AbstractAudioAction {

    @Override
    public String getResourcePrefix() {
        return "/playAudio/";
    }

    @Override
    protected HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.POST};
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
        MediaPlayer mp = null;
        if (isDefaultFile(restOfPath)) {
            mp = MediaPlayer.create(context, getDefaultFile(restOfPath));
        } else {
            File fileHandle = findFile(restOfPath);
            if (fileHandle != null) {
                mp = MediaPlayer.create(context, Uri.fromFile(fileHandle));
            } else {
                response.code(404);
                response.end();
                return;
            }
        }
        mp.start();
        response.code(200);
        response.end();
    }
}
