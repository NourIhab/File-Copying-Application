package com.asset.fc.sp.rest.data.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

/**
 *  
 * @author nour.ihab
 */
@Entity
@Table(name = "FC_H_JOB")
public class JobWrapper {

    @Id
    @Column(name = "JOB_ID", nullable = false)
    @SequenceGenerator(name = "seq", allocationSize = 1, sequenceName = "SEQ_FC_H_JOB")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long jobId;
    @Column
    private String name;
    @Column(name="IMPORT_METHOD")
    private Integer importMethod;
    @Column(name = "STATUS", nullable = false)
    private Integer status;
    @Column
    private String srcFile;
    @Column
    private String destFile;
    @Column
    private Integer maxSpeed;
    @Column
    private float currentSpeed;
    @Column
    private float copyPercentage;
    @Column
    private String owner;
    @Column
    private float fileSize;
    @Column
    private String failReason;
    @CreationTimestamp
    private Timestamp creationDate;

    public JobWrapper() {
    }

    public Long getJobId() {
        return jobId;
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
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

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getCopyPercentage() {
        return copyPercentage;
    }

    public void setCopyPercentage(float copyPercentage) {
        this.copyPercentage = copyPercentage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getFileSize() {
        return fileSize;
    }

    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }


    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

}
