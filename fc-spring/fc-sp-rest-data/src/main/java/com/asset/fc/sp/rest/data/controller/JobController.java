package com.asset.fc.sp.rest.data.controller;

import com.asset.fc.sp.rest.data.constant.ImportMethod;
import com.asset.fc.sp.rest.data.constant.Status;
import com.asset.fc.sp.rest.data.db.facade.JobFacade;
import com.asset.fc.sp.rest.data.model.JobWrapper;
import com.asset.fc.sp.rest.data.task.CopyingThread;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nour.ihab
 */
@RestController                                                     
@RequestMapping(path = "/job")
@CrossOrigin(origins = "http://localhost:4200")
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
    public Optional<JobWrapper> InquireJob(@RequestParam(name = "JobId", required = true, defaultValue = "Unknown") Long jobId) {
        return jobFacade.inquireJob(jobId);
    }

    @GetMapping("/processOfflineJob")
    public String processOfflineJob(@RequestParam(name = "src", required = true, defaultValue = "Unknown") String src, @RequestParam(name = "dest", required = true, defaultValue = "Unknown") String dest, @RequestParam(name = "maxSpeed", required = true, defaultValue = "Unknown") Integer speed, @RequestParam(name = "name", required = true, defaultValue = "Unknown") String name) {

        JobWrapper job = new JobWrapper();
        String message = "";
        try {

            job.setSrcFile(src);
            job.setDestFile(dest);
            job.setMaxSpeed(speed);

            job.setName(name);
            job.setStatus(Status.NEW.getValue());
            job.setOwner("Nour");
            job.setImportMethod(ImportMethod.HTTPREQUEST.getValue());
            jobFacade.createJobId(job);
            CopyingThread copy = context.getBean(CopyingThread.class, job);
            copierPool.execute(copy);
            if (copy.getEx() == null) {
                job.getJobId();
            } else {
                throw copy.getEx();
            }

        } catch (Exception ex) {
        }
        return message = "The job is Accepeted successfuly and its id is = " + job.getJobId();
    }

    @PostMapping(path = "/processOfflineJob", consumes = "application/json", produces = "application/json")
    public Long processOfflineJob(@RequestBody JobWrapper job) {
        String message = "";

        try {

            job.setStatus(Status.NEW.getValue());
            job.setOwner("Nour");
            job.setImportMethod(ImportMethod.HTTPREQUEST.getValue());

            jobFacade.createJobId(job);
            CopyingThread copy = context.getBean(CopyingThread.class, job);
            copierPool.execute(copy);
            if (copy.getEx() == null) {
                job.getJobId();
            } else {
                throw copy.getEx();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return job.getJobId();
    }

    @GetMapping("/processOnlineJob")
    public String processOnlineJob(@RequestParam(name = "src", required = true, defaultValue = "Unknown") String src, @RequestParam(name = "dest", required = true, defaultValue = "Unknown") String dest, @RequestParam(name = "maxSpeed", required = true, defaultValue = "Unknown") Integer speed, @RequestParam(name = "jobName", required = true, defaultValue = "Unknown") String JobName) {
        JobWrapper job = new JobWrapper();
        String message = "";
        try {

            job.setSrcFile(src);
            job.setDestFile(dest);
            job.setMaxSpeed(speed);

            job.setName(JobName);
            job.setStatus(Status.NEW.getValue());
            job.setOwner("Nour");
            job.setImportMethod(ImportMethod.HTTPREQUEST.getValue());
            jobFacade.createJobId(job);
            CopyingThread copy = context.getBean(CopyingThread.class, job);
            copy.run();

        } catch (Exception ex) {

        }
        return message = "The job is Accepeted successfuly and its id is = " + job.getJobId();
    }

    @PostMapping(path = "/processOnlineJob", consumes = "application/json", produces = "application/json")
    public String processOnlineJob(@RequestBody JobWrapper job) {
        String message = "";

        try {

            job.setStatus(Status.NEW.getValue());
            job.setOwner("Nour");
            job.setImportMethod(ImportMethod.HTTPREQUEST.getValue());
            jobFacade.createJobId(job);
            CopyingThread copy = context.getBean(CopyingThread.class, job);
            copy.run();
            if (copy.getEx() == null) {
                job.getJobId();
            } else {
                throw copy.getEx();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return message = " The job is accepted successfully and it's id = " + job.getJobId();
    }
}
