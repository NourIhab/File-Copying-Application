package com.asset.fc.aoe.db.thread;

import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import com.asset.fc.common.parser.ParserFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author nour.ihab
 */
public class ParserThread extends Thread {

    //we used the arrayblockingqueue to protect our resource to be accessed by one thread only and cannot be shared by another thread automatically
    ArrayBlockingQueue<JobWrapper> ParserQueue;
    ArrayBlockingQueue<JobWrapper> CopyQueue;
    AtomicBoolean shutDownSignle;

    public ParserThread(ArrayBlockingQueue<JobWrapper> ParserQueue, AtomicBoolean shutDownSignle, ArrayBlockingQueue<JobWrapper> CopyQueue) {
        this.shutDownSignle = shutDownSignle;
        this.ParserQueue = ParserQueue;
        this.CopyQueue = CopyQueue;

    }

    @Override
    public void run() {

        while (shutDownSignle.get() == true || !ParserQueue.isEmpty()) {

            try {

                JobWrapper parserOutPut = ParserQueue.poll(1000, TimeUnit.MILLISECONDS); // poll the job model from the parser queue
                try {
                    if (parserOutPut == null) {
                        continue;
                    } else {

                        if (JobFacade.updateJobStatus(Status.PARSING, parserOutPut.getJobId()) > 0) {
                            System.out.println(parserOutPut.getJobName() + "Status Updated Successfuly into Parsing in the database");
                        }

                        parserOutPut.setJob(ParserFactory.Parse(parserOutPut.getFile())); //parse the file in the job object of the job wrapper
                        System.out.println(parserOutPut.getJobName() + "Parsed Successfully");
                        parserOutPut.getJobobj();

                        if (JobFacade.updateJobFields(parserOutPut) > 0) {
                            System.out.println(parserOutPut.getJobName() + " Job fileds are Updated successfully in the database");
                        }

                        System.out.println("--------------------------------------------------------------");
                        CopyQueue.put(parserOutPut);
                        System.out.println("Files are parsed successfully in the Copy Queue: " + CopyQueue);
                        JobFacade.updateJobStatus(Status.ENQUEUED_FOR_COPYING, parserOutPut.getJobId());
                        System.out.println(parserOutPut.getJobName() + " Job status is updated sucessfully to Enqeued for copying");
                        System.out.println("--------------------------------------------------------------");
                    }
                } catch (Exception e) {
                    try {
                        JobFacade.updateStatusFailedReson(parserOutPut.getJobId(), Status.FAILED, e.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Failed to update the failes reson");
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
