package com.asset.fc.sp.rest.data.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
@ConfigurationProperties
public class PropertiesCach {

    private String jobFolder;
    private int parserCounter;
    private int copierCounter;

    public String getJobFolder() {
        return jobFolder;
    }

    public void setJobFolder(String jobFolder) {
        this.jobFolder = jobFolder;
    }

    public int getParserCounter() {
        return parserCounter;
    }

    public void setParserCounter(int parserCounter) {
        this.parserCounter = parserCounter;
    }

    public int getCopierCounter() {
        return copierCounter;
    }

    public void setCopierCounter(int copierCounter) {
        this.copierCounter = copierCounter;
    }

}
