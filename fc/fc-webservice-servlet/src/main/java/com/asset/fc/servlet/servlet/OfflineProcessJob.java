package com.asset.fc.servlet.servlet;

import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.Job;
import com.asset.fc.common.model.JobWrapper;
import static com.asset.fc.servlet.manager.WebApplicationManager.copier;
import static com.asset.fc.servlet.manager.WebApplicationManager.parser;
import com.asset.fc.servlet.thread.CopyingThread;
import com.asset.fc.servlet.thread.ParserThread;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.asset.fc.common.utility.Http.getBody;

/**
 *
 * @author nour.ihab
 */
public class OfflineProcessJob extends HttpServlet {

    JobWrapper job = null;
    Job jobObj = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //get the query parameters
        String sourceFile = req.getParameter("src");
        String destFile = req.getParameter("dest");
        String maxSpeed = req.getParameter("maxSpeed");
        String jobName = req.getParameter("name");

        int max = Integer.parseInt(maxSpeed);

        try {
            //set the getted query data into the jobModel
            jobObj = new Job();
            jobObj.setSourceFile(sourceFile);
            jobObj.setDestniationFile(destFile);
            jobObj.setMaxSpeed(max);

            if (jobObj != null) {
                //set the job model to the jobWrapper
                job = new JobWrapper();
                job.setJobobj(jobObj);
                job.setJobName(jobName);
                //generate the id of the job 
                job.setJobId(JobFacade.getNextJobId());

                //insert the name and id in the database
                if (JobFacade.insertJob(job) > 0) {
                    System.out.println(job.getJobName() + " The job is inserted Successfully in the database");
                } else {
                    System.out.println("Failed to insert");
                }
                if (JobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                    System.out.println(" Job status updated Successfully to new in the database");
                } else {
                    System.out.println("Failed to updated status to new");
                }

                //set the extentsion of the job in the database into HTTP request
                job.setJobExtenstion(ImportMethod.HTTPREQUEST);

                if (JobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                    System.out.println("Import Method Updated Successfully in the database");
                } else {
                    System.out.println("Failed to updated the import Method");
                }
                //get the job Object
                job.getJobobj();

                if (JobFacade.updateJobFields(job) > 0) {
                    System.out.println(" Job fileds are Updated successfully in the database");
                } else {
                    System.out.println("Failed to update the job Fields");
                }

                //execute the copier thread
                copier.execute(new CopyingThread(job));
                JobFacade.updateJobStatus(Status.ENQUEUED_FOR_COPYING, job.getJobId());
                System.out.println(" Job status is updated sucessfully to Enqeued for copying");
                System.out.println("--------------------------------------------------------------");
                resp.getWriter().append("Successed, the " + job.getJobName() + "is copied and its ID is: " + job.getJobId()).flush();
            } else {
                System.out.println("The jobWrapper Object is null");
            }

        } catch (Exception ex) {
            try {
                JobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                System.out.println("Failed to update failed reson");
            }
            //sending an error response to the client
            resp.getWriter().append("Failed").flush();

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String type = req.getContentType();//json
        String jobName = req.getParameter("name");

        job = new JobWrapper();
        try {
            job.setJobId(JobFacade.getNextJobId());//set the id in the job model
            job.setJobName(jobName);
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
            //set the extentsion of the job in the database into HTTP request
            job.setJobExtenstion(ImportMethod.HTTPREQUEST);

            if (JobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                System.out.println("Import Method Updated Successfully in the database");
            } else {
                System.out.println("Failed to updated the import Method");
            }
            String body = getBody(req);
            job.setBody(body);
            parser.execute(new ParserThread(job, false));
            JobFacade.updateJobStatus(Status.ENQUEUED_FOR_PARSING, job.getJobId());
            System.out.println(" Job status is updated sucessfully to Enqeued for copying");
            System.out.println("--------------------------------------------------------------");
            resp.getWriter().append("Accepted, the " + job.getJobName() + " has Finished and its ID is: " + job.getJobId()).flush();

            //get the job Object
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }
}
