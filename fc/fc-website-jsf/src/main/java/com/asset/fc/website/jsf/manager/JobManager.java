package com.asset.fc.website.jsf.manager;

import com.asset.fc.website.jsf.constant.ImportMethod;
import com.asset.fc.website.jsf.constant.Status;
import com.asset.fc.website.jsf.db.facade.JobFacade;
import com.asset.fc.website.jsf.model.JobWrapper;
import com.asset.fc.website.jsf.task.CopyingThread;
import java.io.Serializable;
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author nour.ihab
 */
@ManagedBean
@ViewScoped
public class JobManager implements Serializable {
    JobWrapper job;

    public JobManager() {
        job = new JobWrapper();
    }

    @ManagedProperty(value = "#{jobFacade}")
    private JobFacade jobFacade;

    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    public JobFacade getJobFacade() {
        return jobFacade;
    }

    @ManagedProperty(value = "#{webApplicationManager}")
    private WebApplicationManager webApplicationManager;

    public WebApplicationManager getWebApplicationManager() {
        return webApplicationManager;
    }

    public void setWebApplicationManager(WebApplicationManager webApplicationManager) {
        this.webApplicationManager = webApplicationManager;
    }

    @ManagedProperty(value = "#{copyingThread}")
    private CopyingThread copyThread;

    public CopyingThread getCopyThread() {
        return copyThread;
    }

    public void setCopyThread(CopyingThread copyThread) {
        this.copyThread = copyThread;
    }

    HashMap<String, JobWrapper> map;

    public JobWrapper getJob() {
        return job;
    }

    public void setJob(JobWrapper job) {
        this.job = job;
    }

    public HashMap<String, JobWrapper> getMap() {
        return map;
    }

    public void setMap(HashMap<String, JobWrapper> map) {
        this.map = map;
    }

    public void inquireJob() {

        try {
            map = jobFacade.getFileds(job.getJobId());
            job=map.get("JobWrraper");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void processOfflineJob() {

        if (job.getJobobj() != null) {
            try {
                job.setJobId(jobFacade.getNextJobId());
                Long jobId = job.getJobId();

                if (jobFacade.insertJob(job) > 0) {
                    System.out.println(job.getJobName() + " The job is inserted Successfully in the database");
                } else {
                    System.out.println("Failed to insert");
                }
                if (jobFacade.updateJobStatus(Status.NEW, jobId) > 0) {

                    System.out.println(" Job status updated Successfully to new in the database");
                } else {
                    System.out.println("Failed to updated status to new");
                }
                job.setJobExtenstion(ImportMethod.HTTPREQUEST);

                if (jobFacade.updateImportMethod(job.getJobExtenstion(), jobId) > 0) {
                    System.out.println("Import Method Updated Successfully in the database");
                } else {
                    System.out.println("Failed to updated the import Method");
                }
                job.getJobobj();
                if (jobFacade.updateJobFields(job) > 0) {
                    System.out.println(" Job fileds are Updated successfully in the database");
                } else {
                    System.out.println("Failed to update the job Fields");
                }
                copyThread.setJob(job);
                copyThread.getJob();
                webApplicationManager.copier.execute(copyThread);
            } catch (Exception ex) {
                try {
                    jobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, ex.getMessage());
                } catch (Exception ex1) {
                    System.out.println("Failed to update failed reson");
                }
            }
            HashMap<String, Long> map = new HashMap<>();
            map.put("Job_Id", job.getJobId());

        }

    }
}
