package com.asset.fc.engine.manager;

import com.asset.fc.common.manager.PropertiesManager;
import java.util.Properties;

/**
 *
 * @author nour.ihab
 */
public class Engine_PropertiesManager extends PropertiesManager {

    private String jobFolder;

    @Override
    public void ReadProperties(Properties properties) {
        try {
            jobFolder = getString(properties, "job-folder");
        } catch (Exception ex) {
        }
    }

    public String getJobFolder() {
        return jobFolder;
    }

}
