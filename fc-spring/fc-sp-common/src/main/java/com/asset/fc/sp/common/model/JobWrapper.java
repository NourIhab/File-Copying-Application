package com.asset.fc.sp.common.model;

import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
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
    private Job jobobj = new Job();
    private String body;
    private Status status;
    private double currentSpeed;
    private double copyPercentage;
    private double fileSize;
    private String owner;
    private String failReson;
    private String date;

    public JobWrapper() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getCopyPercentage() {
        return copyPercentage;
    }

    public void setCopyPercentage(double copyPercentage) {
        this.copyPercentage = copyPercentage;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFailReson() {
        return failReson;
    }

    public void setFailReson(String failReson) {
        this.failReson = failReson;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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
