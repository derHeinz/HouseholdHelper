package com.heinz.householdhelper.actions;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Heinz on 21.10.2016.
 * Special Action that helps with the use of the other actions.
 */

public class HelpAction extends AbstractAction {

    private Action[] actions;

    public HelpAction(Action[] availableActions) {
        actions = availableActions;
    }

    @Override
    public String getResourcePrefix() {
        return "/help";
    }

    @Override
    public HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.GET};
    }

    @Override
    public String getDescription() {
        return "Helps with other Actions.";
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {

        try {
            List<Action> allActions = new ArrayList<>();
            allActions.addAll(Arrays.asList(actions)); // prevent write-through
            allActions.add(this);

            JSONArray arr = new JSONArray();
            for (Action ac: allActions) {
                JSONObject acJSON = new JSONObject();
                acJSON.put("resource", ac.getResourcePrefix());
                acJSON.put("description", ac.getDescription());
                acJSON.put("methods", Arrays.toString(ac.supportedMethods()));
                arr.put(acJSON);
            }

            JSONObject result = new JSONObject();
            result.put("result", arr);
            response.send(result);
        } catch (JSONException e) {
            // error happened.
            response.code(500);
            response.end();
        }
    }
}
