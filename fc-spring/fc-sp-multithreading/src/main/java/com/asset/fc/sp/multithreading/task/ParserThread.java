package com.asset.fc.sp.multithreading.task;

import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.facade.JobFacade;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.common.model.Job;
import com.asset.fc.sp.common.model.JobWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
@Scope("prototype")
public class ParserThread implements Runnable {

    private JobWrapper job;
    private Job jobobj = null;
    private ObjectMapper mapper;
    private JobFacade jobFacade;
    private CopyingThread copy;
    private ApplicationContext context;
    private ThreadPoolExecutor copierPool;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setCopierPool(ThreadPoolExecutor copierPool) {
        this.copierPool = copierPool;
    }

    @Autowired
    public void setCopy(CopyingThread copy) {
        this.copy = copy;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    public ParserThread(JobWrapper job) {
        this.job = job;
    }

    public ParserThread() {
    }

    @Override
    public void run() {

        try {

            if (jobFacade.updateJobStatus(Status.PARSING, job.getJobId()) > 0) {
                FcLogger.business.info(job.getJobName() + "Status Updated Successfuly into Parsing in the database");
            }

            jobobj = mapper.readValue(job.getFile(), Job.class);
            job.setJob(jobobj);

            FcLogger.business.info(job.getJobName() + "Parsed Successfully");
            job.getJobobj();

            if (jobFacade.updateJobFields(job) > 0) {
                FcLogger.business.info(job.getJobName() + " Job fileds are Updated successfully in the database");
            }
            copierPool.execute(context.getBean(CopyingThread.class, job));
            jobFacade.updateJobStatus(Status.ENQUEUED_FOR_COPYING, job.getJobId());
            FcLogger.business.info(job.getJobName() + " Job status is updated sucessfully to Enqeued for copying");
            FcLogger.business.info("--------------------------------------------------------------");
        } catch (Exception e) {
            String failedReson = e.getMessage();
            try {
                jobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, failedReson);
            } catch (Exception ex1) {
                e.printStackTrace();
            }

        }
    }

}
