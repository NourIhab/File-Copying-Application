package com.asset.fc.aoe.multiple.threading.thread;

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

    public CopyingThread(JobWrapper job) {
        this.job = job;
    }

    @Override
    public void run() {

        double fileSize = 0.0;
        boolean enableDatabase = true;
        try {
            System.out.println("The current job is : " + job.getJobName());
            File file1 = new File(job.getJobobj().getSourceFile()); // get the source file
            System.out.println("The source of the file is :" + job.getJobobj().getSourceFile());
            File file2 = new File(job.getJobobj().getDestniationFile()); // get the desniation file
            System.out.println("The destniation of the file is: " + job.getJobobj().getDestniationFile());
            copyFile(file1, file2, enableDatabase, job);//copy file
            System.out.println("Files are copied successfuly in the database");
            System.out.println("--------------------------------------------------------------");
            System.out.println("\n" + " ----------------------------------------------------");
            System.out.println("If you want to exit the program write the word exit");
        } catch (FileNotFoundException ex) {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // Throwing an exception

}
