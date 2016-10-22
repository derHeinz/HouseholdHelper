package com.heinz.householdhelper.actions;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.io.File;

/**
 * Created by Heinz on 15.10.2016.
 */

public class DeleteFileAction extends AbstractFilenameAction {

    @Override
    public String getResourcePrefix() {
        return "/deleteFile/";
    }

    @Override
    public HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.POST};
    }

    @Override
    public String getDescription() {
        return "Delete a previously stored media-file.";
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
        File file = findFile(restOfPath);
        if (null != file) {
            file.delete();
            response.code(200);
        } else {
            response.code(404);
        }
        response.end();
    }
}
