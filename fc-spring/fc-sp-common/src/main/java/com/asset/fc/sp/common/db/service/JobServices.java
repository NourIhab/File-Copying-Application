package com.asset.fc.sp.common.db.service;

import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.dao.JobDao;
import com.asset.fc.sp.common.model.Inquire;
import com.asset.fc.sp.common.model.JobWrapper;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
public class JobServices {

    private final JobDao dao;

    public JobDao getDao() {
        return dao;
    }

    @Autowired
    public JobServices(JobDao dao) {
        this.dao = dao;
    }

    public Long getNextJobId() throws SQLException, Exception {
        return dao.getNextJobId();

    }

    public Inquire inquireJob(Long jobId) {
        return dao.inquireJob(jobId);
    }

    public JobWrapper getJobFileds(Long jobId) throws SQLException, Exception {
        return dao.getFileds(jobId);

    }

    public int insertJob(JobWrapper job) throws SQLException, Exception {
        return dao.insertJob(job);
    }

    public int updateJobStatus(Status jobStatus, Long jobId) throws SQLException, Exception {
        return dao.updateJobStatus(jobStatus, jobId);
    }

    public int updateJobSpeedPercentatge(float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws SQLException, Exception {
        return dao.updateJobSpeedPercentatge(currentSpeed, copyPercentatge, fileSize, jobId);
    }

    public int updateJobFields(JobWrapper job) throws SQLException, Exception {
        return dao.updateJobFields(job);
    }

    public int updateStatusFailedReson(Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {
        return dao.updateStatusFailedReson(jobId, jobStatus, failedReson);

    }

    public int updateImportMethod(ImportMethod ext, Long jobId) throws SQLException, Exception {
        return dao.updateImportMethod(ext, jobId);
    }
}
