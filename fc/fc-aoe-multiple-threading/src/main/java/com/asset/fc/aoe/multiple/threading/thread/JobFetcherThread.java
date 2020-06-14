package com.asset.fc.aoe.multiple.threading.thread;

import static com.asset.fc.aoe.multiple.threading.manager.AOEApplicationManager.parser;
import com.asset.fc.aoe.multiple.threading.manager.AOEPropertiesManager;
import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nour.ihab
 */
public class JobFetcherThread implements Runnable {

    AOEPropertiesManager pme;

    List<String> list = new ArrayList<>(); //list of jobs to save the jobs added in the queue

    public JobFetcherThread(AOEPropertiesManager pme) {
        this.pme = pme;
    }

    @Override
    public void run() {
        JobWrapper job = null;
        try {

            //Open job-folder and loop over files in it
            File jobFolder = new File(pme.getJobFolder());
            File[] files = jobFolder.listFiles();
            //loop through the files and fetch them
            for (File f : files) {

                //check if the list contains this file return to the for loop else put the file in the parser Queue
                if ((list.contains(f.getName()))) {

                } else {
                    job = new JobWrapper();
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

                    parser.execute(new ParserThread(job));
                    System.out.println("--------------------------------------------------------------");

                    if (JobFacade.updateJobStatus(Status.ENQUEUED_FOR_PARSING, job.getJobId()) > 0) {

                        System.out.println("status updated Successfully to Enqued for parsing in the database");
                    } else {
                        System.out.println("Failed to updated status to Enqued for Parsing");
                    }

                    list.add(f.getName()); // add the jobs that were added in the parser queue into the list

                }
            }

        } catch (Exception ex) {
            try {
                JobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                System.out.println("Failed to update failed reson");
            }
        }

    }
}
