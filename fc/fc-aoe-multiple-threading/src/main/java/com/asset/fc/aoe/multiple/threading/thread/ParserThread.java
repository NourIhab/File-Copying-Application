package com.asset.fc.aoe.multiple.threading.thread;


import static com.asset.fc.aoe.multiple.threading.manager.AOEApplicationManager.copier;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import com.asset.fc.common.parser.ParserFactory;

/**
 *
 * @author nour.ihab
 */
public class ParserThread implements Runnable {

    private JobWrapper job;

    public ParserThread(JobWrapper job) {
        this.job = job;
    }

    @Override
    public void run() {

        try {

            if (JobFacade.updateJobStatus(Status.PARSING, job.getJobId()) > 0) {
                System.out.println(job.getJobName() + "Status Updated Successfuly into Parsing in the database");
            }

            job.setJob(ParserFactory.Parse(job.getFile())); //parse the file in the job object of the job wrapper
            System.out.println(job.getJobName() + "Parsed Successfully");
            job.getJobobj();

            if (JobFacade.updateJobFields(job) > 0) {
                System.out.println(job.getJobName() + " Job fileds are Updated successfully in the database");
            }

            copier.execute(new CopyingThread(job));
            JobFacade.updateJobStatus(Status.ENQUEUED_FOR_COPYING, job.getJobId());
            System.out.println(job.getJobName() + " Job status is updated sucessfully to Enqeued for copying");
            System.out.println("--------------------------------------------------------------");
        } catch (Exception e) {
            try {
                JobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, e.getMessage());
            } catch (Exception ex) {
                System.out.println("Failed to update the failes reson");
            }
        }
    }
}
