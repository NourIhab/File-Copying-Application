package com.asset.fc.common.db.services;

import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.dao.JobDao;
import com.asset.fc.common.model.JobWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author nour.ihab
 */
public class JobServices {

    public static Long getNextJobId(Connection con) throws SQLException, Exception {
        return JobDao.getNextJobId(con);

    }

    public static HashMap<String, String> getJobFileds(Connection con, Long jobId) throws SQLException, Exception {
        return JobDao.getJobFileds(con, jobId);

    }

    public static int insertJob(Connection con, JobWrapper job) throws SQLException, Exception {
        return JobDao.insertJob(con, job);
    }

    public static int updateJobStatus(Status jobStatus, Connection con, Long jobId) throws SQLException, Exception {
        return JobDao.updateJobStatus(jobStatus, con, jobId);
    }

    public static int updateJobSpeedPercentatge(Connection con, float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws SQLException, Exception {
        return JobDao.updateJobSpeedPercentatge(con, currentSpeed, copyPercentatge, fileSize, jobId);
    }

    public static int updateJobFields(Connection con, JobWrapper job) throws SQLException, Exception {
        return JobDao.updateJobFields(con, job);
    }

    public static int updateStatusFailedReson(Connection con, Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {
        return JobDao.updateStatusFailedReson(con, jobId, jobStatus, failedReson);

    }

    public static int updateImportMethod(Connection con, ImportMethod ext, Long jobId) throws SQLException, Exception {
        return JobDao.updateImportMethod(con, ext, jobId);
    }
}
