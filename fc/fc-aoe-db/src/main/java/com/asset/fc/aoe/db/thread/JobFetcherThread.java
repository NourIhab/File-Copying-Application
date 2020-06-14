package com.asset.fc.aoe.db.thread;

import com.asset.fc.aoe.db.manager.AOE_PropertiesManager;
import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author nour.ihab
 */
public class JobFetcherThread extends Thread {

    AOE_PropertiesManager pme;
    ArrayBlockingQueue<JobWrapper> ParserQueue;// array of jobs
    AtomicBoolean shutDownSignle;
    List<String> list = new ArrayList<>(); //list of jobs to save the jobs added in the queue

    public JobFetcherThread(AOE_PropertiesManager pme, ArrayBlockingQueue<JobWrapper> ParserQueue, AtomicBoolean shutDownSignle) {
        this.pme = pme;
        this.ParserQueue = ParserQueue;
        this.shutDownSignle = shutDownSignle;

    }

    @Override
    public void run() {

        while (shutDownSignle.get() == true) {
            JobWrapper job = null;
            try {

                //Open job-folder and loop over files in it
                File jobFolder = new File(pme.getJobFolder());
                File[] files = jobFolder.listFiles();
                //loop through the files and fetch them
                for (File f : files) {
                    job = new JobWrapper();
                    //check if the list contains this file return to the for loop else put the file in the parser Queue
                    if ((list.contains(f.getName()))) {
                        continue;
                    } else {

                        job.setJobId(JobFacade.getNextJobId());//set the id in the job model
                        job.setJobName(f.getName());
                        job.setFile(f);
                        if (JobFacade.insertJob(job) > 0) {
                            System.out.println(job.getJobName() + " Is inserted Successfully in the database");
                        } else {
                            System.out.println("Failed to insert");
                        }

                        if (JobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                            System.out.println(" Job status updated Successfully to new in the database");
                        } else {
                            System.out.println("Failed to updated status to new");
                        }

                        String fileExtention = job.getFile().getName().substring(job.getFile().getName().lastIndexOf(".") + 1);
                        job.setJobExtenstion(ImportMethod.valueOf(fileExtention.toUpperCase()));

                        if (JobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                            System.out.println("Import Method Updated Successfully in the database");
                        } else {
                            System.out.println("Failed to updated the import Method");
                        }

                        ParserQueue.put(job); //put the job model in the parser queue

                        System.out.println("The job model was added successfully to the parser Queue: " + ParserQueue);
                        System.out.println("--------------------------------------------------------------");

                        if (JobFacade.updateJobStatus(Status.ENQUEUED_FOR_PARSING, job.getJobId()) > 0) {

                            System.out.println("status updated Successfully to Enqued for parsing in the database");
                        } else {
                            System.out.println("Failed to updated status to Enqued for Parsing");
                        }

                        list.add(f.getName()); // add the jobs that were added in the parser queue into the list

                    }
                }
                Thread.sleep(10000);

            } catch (Exception ex) {
                try {
                    JobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, ex.getMessage());
                } catch (Exception ex1) {
                    System.out.println("Failed to update failed reson");
                }
            }

        }
    }
}
