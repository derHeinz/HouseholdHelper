package com.heinz.householdhelper.actions;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

/**
 * Used to check whether a given file exists on this server.
 * Created by Heinz on 14.10.2016.
 */

public class HasFileAction extends AbstractFilenameAction {

    @Override
    public String getResourcePrefix() {
        return "/hasFile/";
    }

    @Override
    public HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.GET};
    }

    @Override
    public String getDescription() {
        return "Whether a given media-file exists on the server.";
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
        if (null != findFile(restOfPath)) {
            response.code(200);
        } else {
            response.code(404);
        }
        response.end();
    }
}
