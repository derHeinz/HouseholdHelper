package com.heinz.householdhelper.actions;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to list files stored here.
 * Created by Heinz on 15.10.2016.
 */

public class ListFilesAction extends AbstractAction {

    @Override
    protected String getResourcePrefix() {
        return "/listFiles";
    }

    @Override
    protected HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.GET};
    }

    @Override
    protected boolean appendFilenameRegex() {
        return false;
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
        // ignore the rest of the path
        String[] filenames = context.getFilesDir().list();
        try {
            JSONArray fileList = new JSONArray();
            for (String filename : filenames) {
                JSONObject fileNameJO = new JSONObject();
                fileNameJO.put("filename", filename);
                fileList.put(fileNameJO);
            }

            JSONObject ret = new JSONObject();
            ret.put("files", fileList);
            response.send(ret);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
