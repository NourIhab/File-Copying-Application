package com.asset.fc.common.model;

import com.asset.fc.common.constants.ImportMethod;
import java.io.File;

/**
 *
 * @author nour.ihab
 */
public class JobWrapper {

    private File file;
    private Long jobId;
    private String jobName;
    private ImportMethod jobExtenstion;
    private Job jobobj;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public File getFile() {
        return file;
    }

    public String getJobName() {
        return jobName;
    }

    public ImportMethod getJobExtenstion() {
        return jobExtenstion;
    }

    public Job getJobobj() {
        return jobobj;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobExtenstion(ImportMethod jobExtenstion) {
        this.jobExtenstion = jobExtenstion;
    }

    public void setJobobj(Job jobobj) {
        this.jobobj = jobobj;
    }

    public void setJob(Job jobobj) {
        this.jobobj = jobobj;

    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

}
