package com.asset.fc.website.jsf.manager;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author nour.ihab
 */
@ManagedBean
@ApplicationScoped
public class WebPropertiesManager {

    private int copierCounter;

    @PostConstruct
    public void properties() {
        try {
            String propFileName = "fc.properties";
            Properties prop = new Properties();
            prop.load(WebPropertiesManager.class.getClassLoader().getResourceAsStream(propFileName));
            copierCounter = getInteger(prop, "copier-counter");
            setCopierCounter(copierCounter);
        } catch (Exception ex) {
        }
    }

    public void setCopierCounter(int copierCounter) {
        this.copierCounter = copierCounter;
    }

    public int getCopierCounter() {
        return copierCounter;
    }

    protected Integer getInteger(Properties properties, String propertyName) throws Exception {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            throw new Exception("Could not find property[" + propertyName + "]");
        }
        return Integer.valueOf(value);
    }

}
