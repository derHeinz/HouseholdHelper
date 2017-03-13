package com.heinz.householdhelper.actions;


import android.content.Context;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

/**
 * Created by Heinz on 14.10.2016.
 */

public abstract class AbstractAction implements Action {

    protected Context context;

    /**
     * Register this action to the server.
     *
     * @param server
     * @param ctx
     */
    public void register(AsyncHttpServer server, Context ctx) {
        context = ctx;

        String bindRegex = getResourcePrefix() + getResourceAppend();

        for (HttpMethod hm : supportedMethods()) {
            switch (hm) {
                case GET:
                    server.get(bindRegex, new HttpServerRequestCallback() {
                        @Override
                        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                            request(request, response, request.getPath().substring(getResourcePrefix().length()));
                        }
                    });
                    break;
                case POST:
                    server.post(bindRegex, new HttpServerRequestCallback() {
                        @Override
                        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                            request(request, response, request.getPath().substring(getResourcePrefix().length()));
                        }
                    });
                    break;
            }
        }
    }

    /**
     * Empty default impl.
     * */
    @Override
    public String getResourceAppend() {
        return "";
    }

    /**
     * Do sth. with the request.
     *
     * @param request
     * @param response
     * @param restOfPath
     */
    public abstract void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath);


}
