package com.asset.fc.sp.thymeleaf.controller;

import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.facade.JobFacade;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.common.model.Inquire;
import com.asset.fc.sp.common.model.Job;
import com.asset.fc.sp.common.model.JobWrapper;
import com.asset.fc.sp.thymeleaf.task.CopyingThread;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author nour.ihab
 */
@Controller
@RequestMapping(path = "/")
public class JobController {

    private JobFacade jobFacade;
    private ThreadPoolExecutor copierPool;
    private JobWrapper jobWrapper = new JobWrapper();
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

    @GetMapping(path = "/inquireJob")
    public String inquireJob() {
        return "inquireJob";
    }

    @PostMapping(path = "/inquireJob")
    public String inquireJob(@RequestParam("jobId") Long jobId, Model model) {
        model.addAttribute("jobId", jobId);
        Inquire in = jobFacade.inquireJob(jobId);
        model.addAttribute("Inquire", in);
        return "inquireJob";
    }

    @GetMapping("/processOfflineJob")
    public String processOfflineJob(Model model) {
        model.addAttribute("jobObj", new Job());
        return "processOfflineJob";
    }

    @PostMapping(path = "/processOfflineJob")
    @ResponseStatus(HttpStatus.CREATED)
    public String processOfflineJob(Model model, Job jobObj, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String jobName) {

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
            jobFacade.updateJobFields(job);
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

        model.addAttribute("jobId", jobId);

        return "inquireJob";
    }

    @GetMapping("/processOnlineJob")
    public String processOnlineJob(Model model) {

        model.addAttribute("jobObj", new Job());
        return "processOnlineJob";
    }

    @PostMapping(path = "/processOnlineJob")
    @ResponseStatus(HttpStatus.CREATED)
    public String processOnlineJob(Model model, Job jobObj, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String jobName) {

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
            jobFacade.updateJobFields(job);
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

        model.addAttribute("jobId", jobId);

        return "inquireJob";
    }
}
