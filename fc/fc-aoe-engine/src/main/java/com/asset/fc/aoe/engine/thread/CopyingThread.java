package com.asset.fc.aoe.engine.thread;

import com.asset.fc.common.model.Job;
import static com.asset.fc.common.utility.FileUtility.copyFile;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nour.ihab
 */
public class CopyingThread extends Thread {

    ArrayBlockingQueue<Job> CopyQueue;// array of jobs
    AtomicBoolean shutDownSignle; //flag

    public CopyingThread(ArrayBlockingQueue<Job> CopyQueue, AtomicBoolean shutDownSignle) {
        this.shutDownSignle = shutDownSignle;
        this.CopyQueue = CopyQueue;
    }

    @Override
    public void run() {
        try {
            //loop until the shutdown signle still true or the queue is not empty
            while (shutDownSignle.get() == true || !CopyQueue.isEmpty()) {
                Job jobobj = CopyQueue.poll(1000, TimeUnit.MILLISECONDS);
                //if the jobobj is empty
                if (jobobj == null) {
                    continue; // return to the while loop
                } else {

                    File file1 = new File(jobobj.getSourceFile()); // get the source file
                    System.out.println("The source of the file is :" + jobobj.getSourceFile());
                    File file2 = new File(jobobj.getDestniationFile()); // get the desniation file
                    System.out.println("The destniation of the file is: " + jobobj.getDestniationFile());
                    boolean enableDataBase = false;
                    copyFile(file1, file2, enableDataBase, null);//copy files
                    System.out.println("Files are copied successfuly");
                }
            }
        } catch (InterruptedException ex) {
        } catch (Exception ex) {
            Logger.getLogger(CopyingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Throwing an exception

    }
}
