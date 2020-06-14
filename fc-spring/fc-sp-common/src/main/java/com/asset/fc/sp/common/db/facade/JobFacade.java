package com.asset.fc.sp.common.db.facade;

import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.service.JobServices;
import com.asset.fc.sp.common.model.Inquire;
import com.asset.fc.sp.common.model.JobWrapper;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nour.ihab
 */
@Component
@Transactional
public class JobFacade {

    private final JobServices service;

    public JobServices getService() {
        return service;
    }

    @Autowired
    public JobFacade(JobServices service) {
        this.service = service;
    }

    public int insertJob(JobWrapper job) throws Exception {
        return service.insertJob(job);
    }

    public Inquire inquireJob(Long jobId) {
        return service.inquireJob(jobId);
    }

    public JobWrapper getJobFileds(Long jobId) throws Exception {
        return service.getJobFileds(jobId);
    }

    public Long getNextJobId() throws Exception {
        return service.getNextJobId();
    }

    public int updateJobStatus(Status jobStatus, Long jobId) throws Exception {
        return service.updateJobStatus(jobStatus, jobId);
    }

    public int updateJobSpeedPercentatge(float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws Exception {
        return service.updateJobSpeedPercentatge(currentSpeed, copyPercentatge, fileSize, jobId);
    }

    public int updateJobFields(JobWrapper job) throws Exception {
        return service.updateJobFields(job);
    }

    public int updateStatusFailedReson(Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {
        return service.updateStatusFailedReson(jobId, jobStatus, failedReson);
    }

    public int updateImportMethod(ImportMethod ext, Long jobId) throws SQLException, Exception {
        return service.updateImportMethod(ext, jobId);
    }
}
