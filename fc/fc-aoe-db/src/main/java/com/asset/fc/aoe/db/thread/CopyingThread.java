package com.asset.fc.aoe.db.thread;

import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import static com.asset.fc.common.utility.FileUtility.copyFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author nour.ihab
 */
public class CopyingThread extends Thread {

    ArrayBlockingQueue<JobWrapper> CopyQueue;// array of jobs
    AtomicBoolean shutDownSignle; //flag

    public CopyingThread(ArrayBlockingQueue<JobWrapper> CopyQueue, AtomicBoolean shutDownSignle) {
        this.shutDownSignle = shutDownSignle;
        this.CopyQueue = CopyQueue;

    }

    @Override
    public void run() {

        double fileSize = 0.0;
        boolean enableDatabase = true;
        //loop until the shutdown signle still true or the queue is not empty
        while (shutDownSignle.get() == true || !CopyQueue.isEmpty()) {
            try {

                JobWrapper jobobj = CopyQueue.poll(1000, TimeUnit.MILLISECONDS);

                try {
                    //if the jobobj is empty
                    if (jobobj == null) {
                        //System.out.println("job object is null)");
                        continue; // return to the while loop
                    } else {

                        if (JobFacade.updateJobStatus(Status.COPYING, jobobj.getJobId()) > 0) {
                            System.out.println(jobobj.getJobName() + " Status is updated successfuly to copying");
                        }
                        System.out.println("--------------------------------------------------------------");
                        System.out.println("The current job is : " + jobobj.getJobName());
                        File file1 = new File(jobobj.getJobobj().getSourceFile()); // get the source file
                        System.out.println("The source of the file is :" + jobobj.getJobobj().getSourceFile());
                        File file2 = new File(jobobj.getJobobj().getDestniationFile()); // get the desniation file
                        System.out.println("The destniation of the file is: " + jobobj.getJobobj().getDestniationFile());
                        copyFile(file1, file2, enableDatabase, jobobj);//copy file
                        System.out.println("Files are copied successfuly in the database");
                        System.out.println("--------------------------------------------------------------");
                        if (JobFacade.updateJobStatus(Status.DONE, jobobj.getJobId()) > 0) {
                            System.out.println(jobobj.getJobName() + " Status is updated to Done successfully");
                        }
                        System.out.println("\n" + " ----------------------------------------------------");
                        System.out.println("If you want to exit the program write the word exit");
                    }
                } catch (FileNotFoundException ex) {

                    try {
                        JobFacade.updateStatusFailedReson(jobobj.getJobId(), Status.FAILED, ex.getMessage());
                    } catch (Exception ex1) {
                        System.out.println("Failed to update the failed reson");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // Throwing an exception

    }
}
