package com.asset.fc.website.jsf.db.services;

import com.asset.fc.website.jsf.constant.ImportMethod;
import com.asset.fc.website.jsf.constant.Status;
import com.asset.fc.website.jsf.db.dao.JobDao;
import com.asset.fc.website.jsf.model.JobWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author nour.ihab
 */
@ManagedBean
@ApplicationScoped
public class JobService {

    @ManagedProperty(value = "#{jobDao}")
    private JobDao jobDao;

    public JobDao getJobDao() {
        return jobDao;
    }

    public void setJobDao(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public Long getNextJobId(Connection con) throws SQLException, Exception {
        return jobDao.getNextJobId(con);

    }

    public HashMap<String, String> getJobFileds(Connection con, Long jobId) throws SQLException, Exception {
        return jobDao.getJobFileds(con, jobId);

    }
    
    public HashMap<String, JobWrapper> getFileds(Connection con, Long jobId) throws SQLException, Exception {
        return jobDao.getFileds(con, jobId);

    }

    public int insertJob(Connection con, JobWrapper job) throws SQLException, Exception {
        return jobDao.insertJob(con, job);
    }

    public int updateJobStatus(Status jobStatus, Connection con, Long jobId) throws SQLException, Exception {
        return jobDao.updateJobStatus(jobStatus, con, jobId);
    }

    public int updateJobSpeedPercentatge(Connection con, float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws SQLException, Exception {
        return jobDao.updateJobSpeedPercentatge(con, currentSpeed, copyPercentatge, fileSize, jobId);
    }

    public int updateJobFields(Connection con, JobWrapper job) throws SQLException, Exception {
        return jobDao.updateJobFields(con, job);
    }

    public int updateStatusFailedReson(Connection con, Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {
        return jobDao.updateStatusFailedReson(con, jobId, jobStatus, failedReson);

    }

    public int updateImportMethod(Connection con, ImportMethod ext, Long jobId) throws SQLException, Exception {
        return jobDao.updateImportMethod(con, ext, jobId);
    }
}
