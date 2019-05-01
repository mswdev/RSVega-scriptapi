package org.api.script;

import org.rspeer.script.Script;

import java.io.File;

public class SPXScriptUtil {

    /**
     * Gets the script data path to the folder.
     *
     * @param scriptName The meta data name of the script.
     * @return The script data path to the folder.
     */
    public static String getDataPath(String scriptName) {
        return Script.getDataDirectory() + File.separator + "SPX" + File.separator + scriptName;
    }
}
