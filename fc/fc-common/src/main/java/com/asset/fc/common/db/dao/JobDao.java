package com.asset.fc.common.db.dao;

import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import static com.asset.fc.common.manager.DataBaseManager.closeResoucres;
import static com.asset.fc.common.manager.DataBaseManager.closeResources;
import com.asset.fc.common.model.JobWrapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author nour.ihab
 */
public class JobDao {

    public static Long getNextJobId(Connection con) throws SQLException, Exception {
        PreparedStatement pst = null;
        ResultSet resultSet = null;

        try {
            String sqlIdentifier = "select SEQ_FC_H_JOB.NEXTVAL from dual";
            pst = con.prepareStatement(sqlIdentifier);
            resultSet = pst.executeQuery();
            if (resultSet.next()) { // to read one record from the database and is the record is emoty it returns false
                return resultSet.getLong("NEXTVAL");
            }
        } finally {
            closeResoucres(pst, resultSet);
        }
        return null;
    }

    public static HashMap<String, String> getJobFileds(Connection con, Long jobId) throws SQLException, Exception {
        PreparedStatement pst = null;
        ResultSet resultSet = null;

        try {
            pst = con.prepareStatement("select * from FC_H_JOB where JOB_ID = " + jobId + " ");
            resultSet = pst.executeQuery();
            HashMap<String, String> map = new HashMap<>();

            if (resultSet.next()) {
                int importMethod = resultSet.getInt("IMPORT_METHOD");
                ImportMethod imp = ImportMethod.values()[importMethod - 1];
                map.put("Import_Method", imp.name());
                int status = resultSet.getInt("STATUS");
                Status s = Status.values()[status - 1];
                map.put("Status", s.name());
                map.put("Job_Name", resultSet.getString("NAME"));
                map.put("Source_File", resultSet.getString("SRC_FILE"));
                map.put("Destniation_File", resultSet.getString("DEST_FILE"));
                map.put("Max_Speed", resultSet.getString("MAX_SPEED"));
                map.put("Current_Speed", resultSet.getString("CURRENT_SPEED"));
                map.put("Copy_Percentatge", resultSet.getString("COPY_PERCENTAGE"));
                map.put("Owner", resultSet.getString("OWNER"));
                map.put("File_Size", resultSet.getString("FILE_SIZE"));
                map.put("Fail_Reason", resultSet.getString("FAIL_REASON"));
                map.put("Creation_Date", resultSet.getString("CREATION_DATE"));

            }
            System.out.println("All Records have been selected");
            return map;

        } finally {
            closeResoucres(pst, resultSet);
        }

    }

    public static int insertJob(Connection con, JobWrapper job) throws SQLException, Exception {

        PreparedStatement pst = null;

        try {
            pst = con.prepareStatement("insert into FC_H_JOB (NAME,JOB_ID,STATUS,OWNER,CREATION_DATE) VALUES (?,?,1,'Nour',CURRENT_TIMESTAMP)");
            pst.setString(1, job.getJobName());
            pst.setLong(2, job.getJobId());

            return pst.executeUpdate();

        } finally {
            closeResources(pst);
        }

    }

    public static int updateJobStatus(Status jobStatus, Connection con, Long jobId) throws SQLException, Exception {
        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = con.prepareStatement("update FC_H_JOB set STATUS = ? where JOB_ID = ? and  OWNER = 'Nour'");
            preparedStmt.setInt(1, jobStatus.getValue());
            preparedStmt.setLong(2, jobId);
            return preparedStmt.executeUpdate();
        } finally {
            closeResources(preparedStmt);
        }
    }

    public static int updateJobSpeedPercentatge(Connection con, float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws SQLException, Exception {
        PreparedStatement preparedStmt = null;

        try {
            preparedStmt = con.prepareStatement("update FC_H_JOB set CURRENT_SPEED = ? , COPY_PERCENTAGE = ? ,FILE_SIZE = ? where JOB_ID = ? and  OWNER = 'Nour'");
            preparedStmt.setFloat(1, currentSpeed);
            preparedStmt.setFloat(2, copyPercentatge);
            preparedStmt.setFloat(3, fileSize);
            preparedStmt.setLong(4, jobId);
            return preparedStmt.executeUpdate();
        } finally {
            closeResources(preparedStmt);
        }
    }

    public static int updateJobFields(Connection con, JobWrapper job) throws SQLException, Exception {
        PreparedStatement preparedStmt = null;

        try {
            preparedStmt = con.prepareStatement("update FC_H_JOB set SRC_FILE = ? ,DEST_FILE = ? ,MAX_SPEED = ? where JOB_ID  =? and OWNER = 'Nour'");
            preparedStmt.setString(1, job.getJobobj().getSourceFile());
            preparedStmt.setString(2, job.getJobobj().getDestniationFile());
            preparedStmt.setInt(3, job.getJobobj().getMaxSpeed());
            preparedStmt.setLong(4, job.getJobId());

            return preparedStmt.executeUpdate();

        } finally {
            closeResources(preparedStmt);
        }

    }

    public static int updateStatusFailedReson(Connection con, Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = con.prepareStatement("update FC_H_JOB set STATUS = ? , FAIL_REASON = ? where JOB_ID = ?");
            preparedStmt.setInt(1, jobStatus.getValue());
            preparedStmt.setString(2, failedReson);
            preparedStmt.setLong(3, jobId);
            return preparedStmt.executeUpdate();

        } finally {
            closeResources(preparedStmt);
        }
    }

    public static int updateImportMethod(Connection con, ImportMethod ext, Long jobId) throws SQLException, Exception {
        PreparedStatement pr = null;
        try {
            pr = con.prepareStatement("update FC_H_JOB set IMPORT_METHOD = ? where JOB_ID = ?");
            pr.setInt(1, ext.getValue());
            pr.setLong(2, jobId);
            return pr.executeUpdate();
        } finally {
            closeResources(pr);
        }
    }

}
