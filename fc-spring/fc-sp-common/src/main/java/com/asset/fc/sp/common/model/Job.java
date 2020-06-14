package com.asset.fc.sp.common.model;

public class Job {

    private String sourceFile;
    private String destniationFile;
    private Integer maxSpeed;

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getDestniationFile() {
        return destniationFile;
    }

    public void setDestniationFile(String destniationFile) {
        this.destniationFile = destniationFile;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return "Job{" + "sourceFile=" + sourceFile + ", destniationFile=" + destniationFile + ", maxSpeed=" + maxSpeed + '}';
    }

}
