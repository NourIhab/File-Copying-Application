package com.asset.fc.website.jsf.db.facade;

import com.asset.fc.website.jsf.constant.ImportMethod;
import com.asset.fc.website.jsf.constant.Status;
import com.asset.fc.website.jsf.db.services.JobService;
import com.asset.fc.website.jsf.manager.DataBaseManager;
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
public class JobFacade {

    @ManagedProperty(value = "#{jobService}")
    private JobService jobService;

    public JobService getJobService() {
        return jobService;
    }

    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    @ManagedProperty(value = "#{dataBaseManager}")
    private DataBaseManager dataBaseManager;

    public void setDataBaseManager(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    public int insertJob(JobWrapper job) throws Exception {

        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.insertJob(con, job);

        } finally {
            dataBaseManager.closeConnection(con);
        }
    }

    public HashMap<String, String> getJobFileds(Long jobId) throws Exception {

        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.getJobFileds(con, jobId);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            dataBaseManager.closeConnection(con);
        }

    }

    public HashMap<String, JobWrapper> getFileds(Long jobId) throws Exception {

        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.getFileds(con,jobId);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            dataBaseManager.closeConnection(con);
        }

    }

    public Long getNextJobId() throws Exception {

        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.getNextJobId(con);
        } finally {
            dataBaseManager.closeConnection(con);
        }
    }

    public int updateJobStatus(Status jobStatus, Long jobId) throws Exception {
        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.updateJobStatus(jobStatus, con, jobId);

        } finally {
            dataBaseManager.closeConnection(con);

        }
    }

    public int updateJobSpeedPercentatge(float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws Exception {
        Connection con = null;

        try {
            con = dataBaseManager.openConnection();
            return jobService.updateJobSpeedPercentatge(con, currentSpeed, copyPercentatge, fileSize, jobId);

        } finally {
            dataBaseManager.closeConnection(con);
        }
    }

    public int updateJobFields(JobWrapper job) throws Exception {
        Connection con = null;

        try {
            con = dataBaseManager.openConnection();
            return jobService.updateJobFields(con, job);

        } finally {
            dataBaseManager.closeConnection(con);
        }
    }

    public int updateStatusFailedReson(Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {

        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.updateStatusFailedReson(con, jobId, jobStatus, failedReson);

        } finally {
            dataBaseManager.closeConnection(con);
        }

    }

    public int updateImportMethod(ImportMethod ext, Long jobId) throws SQLException, Exception {

        Connection con = null;
        try {
            con = dataBaseManager.openConnection();
            return jobService.updateImportMethod(con, ext, jobId);

        } finally {
            dataBaseManager.closeConnection(con);
        }
    }
}
