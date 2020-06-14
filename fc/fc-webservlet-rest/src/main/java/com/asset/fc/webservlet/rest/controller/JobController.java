package com.asset.fc.webservlet.rest.controller;

import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.Job;
import com.asset.fc.common.model.JobWrapper;
import com.asset.fc.webservlet.rest.thread.CopyingThread;
import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author nour.ihab
 */
@Path("/job")
public class JobController {

    @GET
    @Path("/inquireJob")
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, String> InquireJob(@QueryParam("jobId") Long jobId) {
        HashMap<String, String> map = new HashMap<>();
        try {
            map = JobFacade.getJobFileds(jobId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return map;
    }

    @GET
    @Path("/ProcessOnlineJob")
    public String ProcessOnlineJob(@QueryParam("Src") String src, @QueryParam("Dest") String dest, @QueryParam("MaxSpeed") Integer speed, @QueryParam("JobName") String JobName) {
        Job jobObj = null;
        JobWrapper job = null;
        Long jobId = null;
        String message = "";
        try {
            //set the getted query data into the jobModel
            jobObj = new Job();
            jobObj.setSourceFile(src);
            jobObj.setDestniationFile(dest);
            jobObj.setMaxSpeed(speed);

            if (jobObj != null) {
                //set the job model to the jobWrapper
                job = new JobWrapper();
                job.setJobobj(jobObj);
                job.setJobName(JobName);
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

                CopyingThread copy = new CopyingThread(job);
                copy.run();
                if (copy.getEx() == null) {
                    jobId = job.getJobId();
                } else {
                    throw copy.getEx();
                }

            } else {
                System.out.println("The jobWrapper Object is null");
            }

        } catch (Exception ex) {
            try {
                JobFacade.updateStatusFailedReson(jobId, Status.FAILED, ex.getMessage());
            } catch (Exception ex1) {
                System.out.println("Failed to update failed reson");
            }
            //sending an error response to the client
            Response.ResponseBuilder serverError = Response.serverError();

        }
        return message = "The job is Accepeted successfuly and its id is = " + jobId;
    }

    @POST
    @Path("/processOnlineJob")
    // @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String processOnlineJob(Job jobObj, @QueryParam("jobName") String jobName) {

        JobWrapper job = new JobWrapper();
        Long jobId = null;
        String message = "";

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

            job.setJob(jobObj);
            CopyingThread copy = new CopyingThread(job);
            copy.run();
            if (copy.getEx() == null) {
                jobId = job.getJobId();
            } else {
                throw copy.getEx();
            }

            //get the job Object
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return message = " The job is accepted successfully and it's id = " + jobId;
    }
}
