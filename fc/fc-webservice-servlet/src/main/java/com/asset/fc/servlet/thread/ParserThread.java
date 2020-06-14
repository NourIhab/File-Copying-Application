package com.asset.fc.servlet.thread;

import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import com.asset.fc.common.parser.ParserFactory;
import static com.asset.fc.servlet.manager.WebApplicationManager.copier;

/**
 *
 * @author nour.ihab
 */
public class ParserThread implements Runnable {

    private JobWrapper job;
    private Exception ex;
    boolean waitingCopier;

    public ParserThread(JobWrapper job, boolean waitingCopier) {
        this.job = job;
        this.waitingCopier = waitingCopier;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    @Override
    public void run() {

        try {

            if (JobFacade.updateJobStatus(Status.PARSING, job.getJobId()) > 0) {
                System.out.println(job.getJobName() + "Status Updated Successfuly into Parsing in the database");
            }

            job.setJob(ParserFactory.Parse(job.getBody(), "json"));
            System.out.println(job.getJobName() + "Parsed Successfully");
            job.getJobobj();

            if (JobFacade.updateJobFields(job) > 0) {
                System.out.println(job.getJobName() + " Job fileds are Updated successfully in the database");
            }
            if (waitingCopier == false) {
                copier.execute(new CopyingThread(job));
            } else {

                CopyingThread copy = new CopyingThread(job);
                if (copy.getEx() == null) {
                    copy.run();
                } else {
                    throw copy.getEx();
                }
            }
            JobFacade.updateJobStatus(Status.ENQUEUED_FOR_COPYING, job.getJobId());
            System.out.println(job.getJobName() + " Job status is updated sucessfully to Enqeued for copying");
            System.out.println("--------------------------------------------------------------");
        } catch (Exception e) {
            setEx(ex);
            try {
                JobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, e.getMessage());
            } catch (Exception ex) {
                System.out.println("Failed to update the failed reson");

            }
        }
    }
}
