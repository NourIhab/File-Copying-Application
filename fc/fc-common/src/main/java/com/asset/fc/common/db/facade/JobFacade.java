package com.asset.fc.common.db.facade;

import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.services.JobServices;
import com.asset.fc.common.manager.DataBaseManager;
import com.asset.fc.common.model.JobWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nour.ihab
 */
public class JobFacade {

    public static int insertJob(JobWrapper job) throws Exception {

        Connection con = null;
        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.insertJob(con, job);

        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }
    }

    public static HashMap<String, String> getJobFileds(Long jobId) throws Exception {

        Connection con = null;
        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.getJobFileds(con, jobId);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }

    }

    public static Long getNextJobId() throws Exception {

        Connection con = null;
        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.getNextJobId(con);
        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }
    }

    public static int updateJobStatus(Status jobStatus, Long jobId) throws Exception {
        Connection con = null;
        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.updateJobStatus(jobStatus, con, jobId);

        } finally {
            DataBaseManager.getInstance().closeConnection(con);

        }
    }

    public static int updateJobSpeedPercentatge(float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws Exception {
        Connection con = null;

        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.updateJobSpeedPercentatge(con, currentSpeed, copyPercentatge, fileSize, jobId);

        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }
    }

    public static int updateJobFields(JobWrapper job) throws Exception {
        Connection con = null;

        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.updateJobFields(con, job);

        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }
    }

    public static int updateStatusFailedReson(Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {

        Connection con = null;
        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.updateStatusFailedReson(con, jobId, jobStatus, failedReson);

        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }

    }

    public static int updateImportMethod(ImportMethod ext, Long jobId) throws SQLException, Exception {

        Connection con = null;
        try {
            con = DataBaseManager.getInstance().openConnection();
            return JobServices.updateImportMethod(con, ext, jobId);

        } finally {
            DataBaseManager.getInstance().closeConnection(con);
        }
    }
}
