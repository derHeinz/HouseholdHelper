package com.heinz.householdhelper.actions;

import java.io.File;

/**
 * Created by Heinz on 14.10.2016.
 */

public abstract class AbstractFilenameAction extends AbstractAction {

    /**
     * Show files stored to the internal storage.
     * @param filename
     * @return
     */
    protected File findFile(String filename) {
        for (File f: context.getFilesDir().listFiles()) {
            if (f.getName().equals(filename)) {
                return f;
            }
        }
        return null;
    }

    @Override
    protected boolean appendFilenameRegex() {
        return true;
    }
}
