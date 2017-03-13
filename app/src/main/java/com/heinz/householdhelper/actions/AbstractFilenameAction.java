package com.heinz.householdhelper.actions;

import android.content.Context;

import com.koushikdutta.async.http.server.AsyncHttpServer;

import java.io.File;

/**
 * Used for filename appends.
 * Created by Heinz on 14.10.2016.
 */

public abstract class AbstractFilenameAction extends AbstractAction {

    /**
     * The regex that represents filenames.
     */
    protected static final String FILENAME_REGEX = "[A-Z,a-z,0-9,.,-,_]+";

    @Override
    public String getResourceAppend() {
        return FILENAME_REGEX;
    }

    /**
     * Show files stored to the internal storage.
     *
     * @param filename
     * @return
     */
    protected File findFile(String filename) {
        for (File f : context.getFilesDir().listFiles()) {
            if (f.getName().equals(filename)) {
                return f;
            }
        }
        return null;
    }

}
