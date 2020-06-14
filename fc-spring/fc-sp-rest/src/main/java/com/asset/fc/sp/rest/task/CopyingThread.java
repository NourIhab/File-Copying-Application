package com.asset.fc.sp.rest.task;

import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.facade.JobFacade;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.common.model.JobWrapper;
import com.asset.fc.sp.common.utility.FileUtility;
import java.io.File;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
@Scope("prototype")
public class CopyingThread implements Runnable {

    private JobWrapper job;
    private FileUtility file;
    private JobFacade facade;
    private Exception ex;

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    @Autowired
    public void setFacade(JobFacade facade) {
        this.facade = facade;
    }

    @Autowired
    public void setFile(FileUtility file) {
        this.file = file;
    }

    public CopyingThread() {
    }

    public CopyingThread(JobWrapper job) {
        this.job = job;
    }

    @Override
    public void run() {

        double fileSize = 0.0;
        boolean enableDatabase = true;
        try {
            facade.updateJobStatus(Status.COPYING, job.getJobId());
            FcLogger.business.info("The current job is : " + job.getJobName());
            File file1 = new File(job.getJobobj().getSourceFile()); // get the source file
            FcLogger.business.info("The source of the file is :" + job.getJobobj().getSourceFile());
            File file2 = new File(job.getJobobj().getDestniationFile()); // get the desniation file
            FcLogger.business.info("The destniation of the file is: " + job.getJobobj().getDestniationFile());
            file.copyFile(file1, file2, enableDatabase, job);//copy file
            FcLogger.business.info("Files are copied successfuly in the database");
            FcLogger.business.info("--------------------------------------------------------------");
            FcLogger.business.info("\n" + " ----------------------------------------------------");
            facade.updateJobStatus(Status.DONE, job.getJobId());
            FcLogger.business.info("If you want to exit the program write the word exit");
        } catch (FileNotFoundException ex) {
            String failedReson = ex.getMessage();
            try {
                facade.updateStatusFailedReson(job.getJobId(), Status.FAILED, failedReson);
            } catch (Exception ex1) {
                try {
                    facade.updateStatusFailedReson(job.getJobId(), Status.FAILED, failedReson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ex1.printStackTrace();
            }
        } catch (Exception ex) {
            setEx(ex);
            ex.printStackTrace();

        }
    }

}
