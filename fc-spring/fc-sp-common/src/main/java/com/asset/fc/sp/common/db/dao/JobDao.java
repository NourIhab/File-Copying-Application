    package com.asset.fc.sp.common.db.dao;

import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.model.Inquire;
import com.asset.fc.sp.common.model.JobWrapper;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
public class JobDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long getNextJobId() throws SQLException, Exception {

        String sqlIdentifier = "select SEQ_FC_H_JOB.NEXTVAL from dual";
        return jdbcTemplate.queryForObject(sqlIdentifier, Long.class);

    }

    public JobWrapper getFileds(Long jobId) throws SQLException, Exception {
        String sqlIdentifier = ("select * from FC_H_JOB where JOB_ID = " + jobId + " ");
        return (JobWrapper) jdbcTemplate.queryForObject(sqlIdentifier, new Object[]{jobId}, new BeanPropertyRowMapper(JobWrapper.class));

    }

    public Inquire inquireJob(Long jobId) {
        String query = ("select * from FC_H_JOB where JOB_ID = ? ");
        return jdbcTemplate.queryForObject(query, new Object[]{jobId}, BeanPropertyRowMapper.newInstance(Inquire.class));
    }

    public int updateJobStatus(Status jobStatus, Long jobId) throws SQLException, Exception {
        String sqlIdentifier = ("update FC_H_JOB set STATUS = ? where JOB_ID = ? and  OWNER = 'Nour'");
        return jdbcTemplate.update(sqlIdentifier, jobStatus.getValue(), jobId);
    }

    public int updateJobSpeedPercentatge(float currentSpeed, float copyPercentatge, float fileSize, Long jobId) throws SQLException, Exception {

        String sqlIdentifier = ("update FC_H_JOB set CURRENT_SPEED = ? , COPY_PERCENTAGE = ? ,FILE_SIZE = ? where JOB_ID = ? and  OWNER = 'Nour'");
        return jdbcTemplate.update(sqlIdentifier, currentSpeed, copyPercentatge, fileSize, jobId);

    }

    public int updateJobFields(JobWrapper job) throws SQLException, Exception {
        String sqlIdentifier = ("update FC_H_JOB set SRC_FILE = ? ,DEST_FILE = ? ,MAX_SPEED = ? where JOB_ID  =? and OWNER = 'Nour'");
        return jdbcTemplate.update(sqlIdentifier, job.getJobobj().getSourceFile(), job.getJobobj().getDestniationFile(), job.getJobobj().getMaxSpeed(), job.getJobId());

    }

    public int insertJob(JobWrapper job) throws SQLException, Exception {
        String sqlIdentifier = ("insert into FC_H_JOB (NAME,JOB_ID,STATUS,OWNER) VALUES (?,?,1,'Nour')");
        return jdbcTemplate.update(sqlIdentifier, job.getJobName(), job.getJobId());
    }

    public int updateStatusFailedReson(Long jobId, Status jobStatus, String failedReson) throws SQLException, Exception {

        String sqlIdentifier = ("update FC_H_JOB set STATUS = ? , FAIL_REASON = ? where JOB_ID = ?");
        return jdbcTemplate.update(sqlIdentifier, jobId, jobStatus.getValue(), failedReson);
    }

    public int updateImportMethod(ImportMethod ext, Long jobId) throws SQLException, Exception {

        String sqlIdentifier = ("update FC_H_JOB set IMPORT_METHOD = ? where JOB_ID = ?");
        return jdbcTemplate.update(sqlIdentifier, ext.getValue(), jobId);
    }
}
