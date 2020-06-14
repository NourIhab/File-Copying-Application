package com.asset.fc.servlet.manager;

import com.asset.fc.common.manager.PropertiesManager;
import java.util.Properties;

/**
 *
 * @author nour.ihab
 */
public class WebPropertiesManager extends PropertiesManager {

    private int parserCounter;
    private int copierCounter;
    private String jobFolder;

    @Override
    public void ReadProperties(Properties p) {
        try {
            parserCounter = getInteger(p, "parser-counter");
            copierCounter = getInteger(p, "copier-counter");
            jobFolder = getString(p, "job-folder");
        } catch (Exception ex) {
        }
    }

    public int getParserCounter() {
        return parserCounter;
    }

    public int getCopierCounter() {
        return copierCounter;
    }

    public String getJobFolder() {
        return jobFolder;
    }

}
