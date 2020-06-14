package com.asset.fc.sp.rest.data.task;

import com.asset.fc.sp.rest.data.constant.Status;
import com.asset.fc.sp.rest.data.db.facade.JobFacade;
import com.asset.fc.sp.rest.data.logger.FcLogger;
import com.asset.fc.sp.rest.data.model.JobWrapper;
import com.asset.fc.sp.rest.data.utlity.FileUtility;

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
            
            job.setStatus(Status.COPYING.getValue());
            FcLogger.business.info("The current job is : " + job.getName());
            File file1 = new File(job.getSrcFile()); // get the source file
            FcLogger.business.info("The source of the file is :" + job.getSrcFile());
            File file2 = new File(job.getDestFile()); // get the desniation file
            FcLogger.business.info("The destniation of the file is: " + job.getDestFile());
            file.copyFile(file1, file2, enableDatabase, job);//copy file
            FcLogger.business.info("Files are copied successfuly in the database");
            FcLogger.business.info("--------------------------------------------------------------");
            FcLogger.business.info("\n" + " ----------------------------------------------------");
            job.setStatus(Status.DONE.getValue());
            facade.createJobId(job);
            FcLogger.business.info("If you want to exit the program write the word exit");
        } catch (FileNotFoundException ex) {

        } catch (Exception ex) {
            setEx(ex);
            ex.printStackTrace();

        }
    }

}
