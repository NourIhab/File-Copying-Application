package com.asset.fc.website.jsp.thread;

import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import static com.asset.fc.common.utility.FileUtility.copyFile;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author nour.ihab
 */
public class CopyingThread implements Runnable {

    private JobWrapper job;
    private Exception ex;

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
        //loop until the shutdown signle still true or the queue is not empty

        try {

            if (JobFacade.updateJobStatus(Status.COPYING, job.getJobId()) > 0) {
                System.out.println(job.getJobName() + " Status is updated successfuly to copying");
            }
            System.out.println("--------------------------------------------------------------");
            System.out.println("The current job is : " + job.getJobName());
            File file1 = new File(job.getJobobj().getSourceFile()); // get the source file
            System.out.println("The source of the file is :" + job.getJobobj().getSourceFile());
            File file2 = new File(job.getJobobj().getDestniationFile()); // get the desniation file
            System.out.println("The destniation of the file is: " + job.getJobobj().getDestniationFile());
            copyFile(file1, file2, enableDatabase, job);//copy file
            System.out.println("Files are copied successfuly in the database");
            System.out.println("--------------------------------------------------------------");
            if (JobFacade.updateJobStatus(Status.DONE, job.getJobId()) > 0) {
                System.out.println(job.getJobName() + " Status is updated to Done successfully");
            }
            System.out.println("\n" + " ----------------------------------------------------");
            System.out.println("If you want to exit the program write the word exit");
        } catch (FileNotFoundException ex) {

            try {
                JobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                System.out.println("Failed to update the failed reson");
            }

        } catch (Exception ex) {
            setEx(ex);
            ex.printStackTrace();
        }
    }
    // Throwing an exception

}
