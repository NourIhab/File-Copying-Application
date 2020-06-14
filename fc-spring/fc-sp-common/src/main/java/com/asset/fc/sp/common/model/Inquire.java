package com.asset.fc.sp.common.model;

import java.sql.Timestamp;

/**
 *
 * @author nour.ihab
 */
public class Inquire {

    private Long jobId;
    private String name;
    private Integer importMethod;
    private Integer status;
    private String srcFile;
    private String destFile;
    private Integer maxSpeed;
    private Double currentSpeed;
    private Double copyPercentage;
    private String owner;
    private Integer fileSize;
    private String failReason;
    private Timestamp creationDate;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImportMethod() {
        return importMethod;
    }

    public void setImportMethod(Integer importMethod) {
        this.importMethod = importMethod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }

    public String getDestFile() {
        return destFile;
    }

    public void setDestFile(String destFile) {
        this.destFile = destFile;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(Double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Double getCopyPercentage() {
        return copyPercentage;
    }

    public void setCopyPercentage(Double copyPercentage) {
        this.copyPercentage = copyPercentage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

}
