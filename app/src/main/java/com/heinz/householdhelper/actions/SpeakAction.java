package com.heinz.householdhelper.actions;

import android.util.Base64;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

/**
 * Created by Heinz on 20.10.2016.
 * Send this listener a base64 coded text within the post body and it will speak it using the built-in TTS system.
 */
public class SpeakAction extends AbstractSpeakAction {

    @Override
    public String getResourcePrefix() {
        return "/speak";
    }

    @Override
    public HttpMethod[] supportedMethods() {
        return new HttpMethod[]{HttpMethod.POST};
    }

    @Override
    protected boolean appendFilenameRegex() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Speaks out something that it receives via a base64 body.";
    }

    @Override
    public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {

        // load the post body text to speak.
        AsyncHttpRequestBody body = request.getBody();
        if (!(body instanceof StringBody)) {
            response.code(400);
            response.end();
            return;
        }
        StringBody textBody = (StringBody) request.getBody();
        // decode BASE64 string into utf-8 string.
        byte[] bytes = Base64.decode(textBody.get().getBytes(), Base64.DEFAULT);
        String decodedString = new String(bytes);
        speak(decodedString);

        // return after speak
        response.code(200);
        response.end();
    }
}
