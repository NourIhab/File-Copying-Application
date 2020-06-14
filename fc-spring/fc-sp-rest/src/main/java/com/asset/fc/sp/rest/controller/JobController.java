package com.asset.fc.sp.rest.controller;

import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.facade.JobFacade;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.common.model.Inquire;
import com.asset.fc.sp.common.model.Job;
import com.asset.fc.sp.common.model.JobWrapper;
import com.asset.fc.sp.rest.task.CopyingThread;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nour.ihab
 */
@RestController
@RequestMapping(path = "/job")
public class JobController {

    private JobFacade jobFacade;
    private ThreadPoolExecutor copierPool;

    private ApplicationContext context;

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    @Autowired
    public void setCopierPool(ThreadPoolExecutor copierPool) {
        this.copierPool = copierPool;
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @GetMapping("/inquireJob")
    public Inquire InquireJob(@RequestParam(name = "JobId", required = true, defaultValue = "Unknown") Long jobId) {
        return jobFacade.inquireJob(jobId);
    }

    @GetMapping("/processOfflineJob")
    public String processOfflineJob(@RequestParam(name = "src", required = true, defaultValue = "Unknown") String src, @RequestParam(name = "dest", required = true, defaultValue = "Unknown") String dest, @RequestParam(name = "maxSpeed", required = true, defaultValue = "Unknown") Integer speed, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String JobName) {
        Job jobObj = null;
        JobWrapper job = new JobWrapper();
        Long jobId = null;
        String message = "";
        try {
            //set the getted query data into the jobModel
            jobObj = new Job();
            jobObj.setSourceFile(src);
            jobObj.setDestniationFile(dest);
            jobObj.setMaxSpeed(speed);

            if (jobObj != null) {
                job = new JobWrapper();
                job.setJobobj(jobObj);
                job.setJobName(JobName);
                job.setJobId(jobFacade.getNextJobId());
                jobId = job.getJobId();

                if (jobFacade.insertJob(job) > 0) {
                    FcLogger.business.info(job.getJobName() + " The job is inserted Successfully in the database");
                } else {
                    FcLogger.business.info("Failed to insert" + job.getJobName());
                }
                if (jobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                    FcLogger.business.info(" Job status updated Successfully to new in the database");
                } else {
                    FcLogger.business.info("Failed to updated status to new");
                }
                job.setJobExtenstion(ImportMethod.HTTPREQUEST);

                if (jobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                    FcLogger.business.info("Import Method Updated Successfully in the database");
                } else {
                    FcLogger.business.info("Failed to updated the import Method");
                }
                //get the job Object
                job.getJobobj();

                if (jobFacade.updateJobFields(job) > 0) {
                    FcLogger.business.info(" Job fileds are Updated successfully in the database");
                } else {
                    FcLogger.business.info("Failed to update the job Fields");
                }

                CopyingThread copy = context.getBean(CopyingThread.class, job);
                copierPool.execute(copy);
                if (copy.getEx() == null) {
                    jobId = job.getJobId();
                } else {
                    throw copy.getEx();
                }

            } else {
                FcLogger.business.info("The jobWrapper Object is null");
            }

        } catch (Exception ex) {
            try {
                jobFacade.updateStatusFailedReson(jobId, Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                FcLogger.business.info("Failed to update failed reson");
            }
        }
        return message = "The job is Accepeted successfuly and its id is = " + jobId;
    }

    @PostMapping(path = "/processOfflineJob", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String processOfflineJob(@RequestBody Job jobObj, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String jobName) {

        JobWrapper job = new JobWrapper();
        Long jobId = null;
        String message = "";

        try {
            job.setJobId(jobFacade.getNextJobId());
            jobId = job.getJobId();
            job.setJobName(jobName);
            if (jobFacade.insertJob(job) > 0) {
                FcLogger.business.info(job.getJobName() + " Is inserted Successfully in the database");
            } else {
                FcLogger.business.info("Failed to insert");
            }
            if (jobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                FcLogger.business.info(" Job status updated Successfully to new in the database");
            } else {
                FcLogger.business.info("Failed to updated status to new");
            }
            job.setJobExtenstion(ImportMethod.HTTPREQUEST);

            if (jobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                FcLogger.business.info("Import Method Updated Successfully in the database");
            } else {
                FcLogger.business.info("Failed to updated the import Method");
            }

            job.setJob(jobObj);
            CopyingThread copy = context.getBean(CopyingThread.class, job);
            copierPool.execute(copy);
            if (copy.getEx() == null) {
                jobId = job.getJobId();
            } else {
                throw copy.getEx();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return message = " The job is accepted successfully and it's id = " + jobId;
    }

    @GetMapping("/processOnlineJob")
    public String processOnlineJob(@RequestParam(name = "src", required = true, defaultValue = "Unknown") String src, @RequestParam(name = "dest", required = true, defaultValue = "Unknown") String dest, @RequestParam(name = "maxSpeed", required = true, defaultValue = "Unknown") Integer speed, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String JobName) {
        Job jobObj = null;
        JobWrapper job = new JobWrapper();
        Long jobId = null;
        String message = "";
        try {
            //set the getted query data into the jobModel
            jobObj = new Job();
            jobObj.setSourceFile(src);
            jobObj.setDestniationFile(dest);
            jobObj.setMaxSpeed(speed);

            if (jobObj != null) {
                job = new JobWrapper();
                job.setJobobj(jobObj);
                job.setJobName(JobName);
                job.setJobId(jobFacade.getNextJobId());
                jobId = job.getJobId();

                if (jobFacade.insertJob(job) > 0) {
                    FcLogger.business.info(job.getJobName() + " The job is inserted Successfully in the database");
                } else {
                    FcLogger.business.info("Failed to insert");
                }
                if (jobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                    FcLogger.business.info(" Job status updated Successfully to new in the database");
                } else {
                    FcLogger.business.info("Failed to updated status to new");
                }

                //set the extentsion of the job in the database into HTTP request
                job.setJobExtenstion(ImportMethod.HTTPREQUEST);

                if (jobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                    FcLogger.business.info("Import Method Updated Successfully in the database");
                } else {
                    FcLogger.business.info("Failed to updated the import Method");
                }
                //get the job Object
                job.getJobobj();

                if (jobFacade.updateJobFields(job) > 0) {
                    FcLogger.business.info(" Job fileds are Updated successfully in the database");
                } else {
                    FcLogger.business.info("Failed to update the job Fields");
                }

                CopyingThread copy = context.getBean(CopyingThread.class, job);
                copy.run();

                if (copy.getEx() == null) {
                    jobId = job.getJobId();
                } else {
                    throw copy.getEx();
                }

            } else {
                FcLogger.business.info("The jobWrapper Object is null");
            }

        } catch (Exception ex) {
            try {
                jobFacade.updateStatusFailedReson(jobId, Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                FcLogger.business.info("Failed to update failed reson");
            }
        }
        return message = "The job is Accepeted successfuly and its id is = " + jobId;
    }

    @PostMapping(path = "/processOnlineJob", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String processOnlineJob(@RequestBody Job jobObj, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String jobName) {

        JobWrapper job = new JobWrapper();
        Long jobId = null;
        String message = "";

        try {
            job.setJobId(jobFacade.getNextJobId());//set the id in the job model
            jobId = job.getJobId();
            job.setJobName(jobName);
            if (jobFacade.insertJob(job) > 0) {
                FcLogger.business.info(job.getJobName() + " Is inserted Successfully in the database");
            } else {
                FcLogger.business.info("Failed to insert");
            }
            if (jobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                FcLogger.business.info(" Job status updated Successfully to new in the database");
            } else {
                FcLogger.business.info("Failed to updated status to new");
            }
            //set the extentsion of the job in the database into HTTP request
            job.setJobExtenstion(ImportMethod.HTTPREQUEST);

            if (jobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                FcLogger.business.info("Import Method Updated Successfully in the database");
            } else {
                FcLogger.business.info("Failed to updated the import Method");
            }
            job.setJob(jobObj);
            CopyingThread copy = context.getBean(CopyingThread.class, job);
            copy.run();
            if (copy.getEx() == null) {
                jobId = job.getJobId();
            } else {
                throw copy.getEx();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return message = " The job is accepted successfully and it's id = " + jobId;
    }
}
