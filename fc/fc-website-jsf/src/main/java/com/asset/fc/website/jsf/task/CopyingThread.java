package com.asset.fc.website.jsf.task;

import com.asset.fc.website.jsf.constant.Status;
import com.asset.fc.website.jsf.db.facade.JobFacade;
import com.asset.fc.website.jsf.model.JobWrapper;
import com.asset.fc.website.jsf.utility.FileUtility;
import java.io.File;
import java.io.FileNotFoundException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author nour.ihab
 */
@ManagedBean
@ApplicationScoped
public class CopyingThread implements Runnable {

    private JobWrapper job;
    private Exception ex;
    @ManagedProperty(value = "#{jobFacade}")
    private JobFacade jobFacade;

    public JobFacade getJobFacade() {
        return jobFacade;
    }

    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }
    
     @ManagedProperty(value = "#{fileUtility}")
    private FileUtility fileUtility;

    public FileUtility getFileUtility() {
        return fileUtility;
    }

    public void setFileUtility(FileUtility fileUtility) {
        this.fileUtility = fileUtility;
    }

    public JobWrapper getJob() {
        return job;
    }

    public void setJob(JobWrapper job) {
        this.job = job;
    }
     

    public CopyingThread() {
        this.job=new JobWrapper();
    }

    public CopyingThread(JobWrapper job) {
        this.job = job;

    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    @Override
    public void run() {

        double fileSize = 0.0;
        boolean enableDatabase = true;
        try {

            if (jobFacade.updateJobStatus(Status.COPYING, job.getJobId()) > 0) {
                System.out.println(job.getJobName() + " Status is updated successfuly to copying");
            }
            System.out.println("--------------------------------------------------------------");
            System.out.println("The current job is : " + job.getJobName());
            File file1 = new File(job.getJobobj().getSourceFile());
            System.out.println("The source of the file is :" + job.getJobobj().getSourceFile());
            File file2 = new File(job.getJobobj().getDestniationFile());
            System.out.println("The destniation of the file is: " + job.getJobobj().getDestniationFile());
            fileUtility.copyFile(file1, file2, enableDatabase, job);
            System.out.println("Files are copied successfuly in the database");
            System.out.println("--------------------------------------------------------------");
            if (jobFacade.updateJobStatus(Status.DONE, job.getJobId()) > 0) {
                System.out.println(job.getJobName() + " Status is updated to Done successfully");
            }
            System.out.println("\n" + " ----------------------------------------------------");
            System.out.println("If you want to exit the program write the word exit");

        } catch (FileNotFoundException ex) {

            try {
                jobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                System.out.println("Failed to update the failed reson");
            }

        } catch (Exception ex) {
            setEx(ex);
            ex.printStackTrace();
        }
    }
}
