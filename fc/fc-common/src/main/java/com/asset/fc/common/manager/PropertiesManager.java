package com.asset.fc.common.manager;

import java.io.IOException;
import java.util.Properties;

public abstract class PropertiesManager {

    private static PropertiesManager instance; //instance from the class

    public static PropertiesManager getInstance() {
        return instance; // create an instance from the class inside the class as it is singleton
    }

    //load and read the properties file
    public final void loadProperties(String propFileName) throws IOException, Exception {
        if (instance == null) {
            Properties prop = new Properties();
            prop.load(PropertiesManager.class.getClassLoader().getResourceAsStream(propFileName)); // load the properties file
            ReadProperties(prop); // call the read properties function 
        }
    }

    //wrap the value which is integer into an intger object
    protected Integer getInteger(Properties properties, String propertyName) throws Exception {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            throw new Exception("Could not find property[" + propertyName + "]");
        }
        return Integer.valueOf(value);
    }

    //wrap the value which is a string into a string object
    protected String getString(Properties properties, String propertyName) throws Exception {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            throw new Exception("Could not find property[" + propertyName + "]");
        }
        return value;
    }

    public abstract void ReadProperties(Properties p);

}
