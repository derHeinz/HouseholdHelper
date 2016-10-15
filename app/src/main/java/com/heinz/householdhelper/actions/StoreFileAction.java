package com.heinz.householdhelper.actions;

import android.util.Base64;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Store files to the internal storage of the phone for later use.
 * https://developer.android.com/training/basics/data-storage/files.html.
 * Creates the file denoted by the path, e.g. /storeFile/bla.wav stores a file called bla.wav.
 * JSON structure is like: { file="<base64encoded>" }.  Where the base64encoded string is the content of the file.
 * Created by Heinz on 14.10.2016.
 */

public class StoreFileAction extends AbstractFilenameAction {

    @Override
    public String getResourcePrefix() {
        return "/storeFile/";
    }

    @Override
    protected HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.POST};
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {

        // check for the body to be in correct matter (e.g. content type needs to be "application/json")
        AsyncHttpRequestBody body = request.getBody();
        if (!(body instanceof JSONObjectBody)) {
            response.code(400);
            response.end();
            return;
        }

        // delete old file if available
        File tempOld = findFile(restOfPath);
        if (tempOld != null) {
            tempOld.delete();
        }

        // parse
        JSONObjectBody jsonBody = (JSONObjectBody) body;
        try {
            String fileContentBase64 = (String) jsonBody.get().get("file");
            byte[] fileContent = Base64.decode(fileContentBase64.getBytes(), Base64.DEFAULT);

            File newFile = context.getFileStreamPath(restOfPath);
            FileOutputStream outputStream;

            try {
                outputStream = new FileOutputStream(newFile);
                outputStream.write(fileContent);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        response.code(200);
        response.end();
    }

}
