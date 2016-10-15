package com.heinz.householdhelper.actions;

import com.heinz.householdhelper.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heinz on 14.10.2016.
 */

public abstract class AbstractAudioAction extends AbstractFilenameAction {

    private static Map<String, Integer> defaultAudioFiles;
    static {
        defaultAudioFiles = new HashMap<>();
        defaultAudioFiles.put("beep.wav", R.raw.beep);
        defaultAudioFiles.put("pop.wav", R.raw.pop);
    }

    /**
     * Whether it is a default file or not.
     * @param filename
     * @return
     */
    protected boolean isDefaultFile(String filename) {
        if (defaultAudioFiles.containsKey(filename)) {
            return true;
        }
        return false;
    }

    /**
     * Get resource ID for the default file denoted by the given filename.
     * @param filename
     * @return
     */
    protected int getDefaultFile(String filename) {
        return defaultAudioFiles.get(filename);
    }
}
