package com.heinz.householdhelper.actions;

import android.content.Context;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServer;

/**
 * Created by Heinz on 14.10.2016.
 */

public interface Action {

    /**
     * Register this action to the server.
     *
     * @param server
     * @param ctx
     */
    void register(AsyncHttpServer server, Context ctx);

    /**
     * Returns description.
     *
     * @return
     */
    String getDescription();


    /**
     * Which http methods are supported. Typically POST, GET, PUT, etc.s
     *
     * @return
     */
    HttpMethod[] supportedMethods();

    /**
     * Resource path.
     *
     * @return
     */
    String getResourcePrefix();

    /**
     * Resource path.
     *
     * @return
     */
    String getResourceAppend();
}
