package com.heinz.householdhelper.actions;

import com.heinz.householdhelper.HttpMethod;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Heinz on 11.03.2017.
 */

public class AbstractFilenameActionTest {

    private AbstractFilenameAction createTestee() {
        return new AbstractFilenameAction() {
            @Override
            public void request(AsyncHttpServerRequest request, AsyncHttpServerResponse response, String restOfPath) {
                // not used
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public HttpMethod[] supportedMethods() {
                return new HttpMethod[0];
            }

            @Override
            public String getResourcePrefix() {
                return "test";
            }
        };
    }

    @Test
    public void testGetResourcePrefix() {
        assertEquals(AbstractFilenameAction.FILENAME_REGEX, createTestee().getResourceAppend());
    }
}
