package com.asset.fc.aoe.engine.thread;

import com.asset.fc.aoe.engine.manager.AOE_PropertiesManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nour.ihab
 */
public class JobFetcherThread extends Thread {

    AOE_PropertiesManager pme;
    ArrayBlockingQueue<File> ParserQueue;// array of files
    AtomicBoolean shutDownSignle;
    List<String> list = new ArrayList<>(); //list of files

    public JobFetcherThread(AOE_PropertiesManager pme, ArrayBlockingQueue<File> ParserQueue, AtomicBoolean shutDownSignle) {
        this.pme = pme;
        this.ParserQueue = ParserQueue;
        this.shutDownSignle = shutDownSignle;
    }

    @Override
    public void run() {

        try {

            while (shutDownSignle.get() == true) {
                //Open job-folder and loop over files in it
                File jobFolder = new File(pme.getJobFolder());
                File[] files = jobFolder.listFiles();
                //loop through the files and fetch them
                for (File f : files) {
                    //check if the list contains this file return to the for loop else put the file in the parser Queue
                    if ((list.contains(f.getName()))) {
                        continue;
                    } else {
                        ParserQueue.put(f); //put the files in the parser queue
                        System.out.println("The files are added Successfully in the Parser Queue" + ParserQueue);
                        list.add(f.getName()); // add the files that were added in the parser queue into the list
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(JobFetcherThread.class.getName()).log(Level.SEVERE, null, ex);
        } //}
        catch (NullPointerException e) {
            // Throwing an exception 
            System.out.println("Exception is caught in Job Fetcher");
        }
    }
}
