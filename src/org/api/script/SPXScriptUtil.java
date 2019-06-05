package org.api.script;

import org.rspeer.script.Script;

import java.io.File;

public class SPXScriptUtil {

    /**
     * Gets the spx data path to the folder.
     *
     * @return The spx data path to the folder.
     */
    public static String getSPXDataPath() {
        return Script.getDataDirectory() + File.separator + "SPX";
    }

    /**
     * Gets the script data path to the folder.
     *
     * @param scriptName The meta data name of the script.
     * @return The script data path to the folder.
     */
    public static String getScriptDataPath(String scriptName) {
        return getSPXDataPath() + File.separator + scriptName;
    }
}
