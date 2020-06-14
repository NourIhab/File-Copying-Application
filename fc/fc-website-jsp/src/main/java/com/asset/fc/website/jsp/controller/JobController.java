package com.asset.fc.website.jsp.controller;

import com.asset.fc.common.constants.ImportMethod;
import com.asset.fc.common.constants.Status;
import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.Job;
import com.asset.fc.common.model.JobWrapper;
import static com.asset.fc.website.jsp.manager.WebApplicationManager.copier;
import com.asset.fc.website.jsp.thread.CopyingThread;

import java.util.HashMap;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 *
 * @author nour.ihab
 */
@Path("/job")
public class JobController {

    @GET
    @Path("/inquireJob")
    @Produces(MediaType.TEXT_HTML)
    public Viewable inquireJob() {
        return new Viewable("/inquireJob");
    }

    @POST
    @Path("/inquireJob")
    @Produces(MediaType.TEXT_HTML)
    public Viewable inquireJob(@FormParam("jobID") Long jobId) {
        HashMap<String, String> map = new HashMap<>();
        try {
            map = JobFacade.getJobFileds(jobId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new Viewable("/inquireJob", map);
    }

    @GET
    @Path("/processOfllineJob")
    @Produces(MediaType.TEXT_HTML)
    public Viewable processOfflineJob() {
        return new Viewable("/processOfllineJob");
    }

    @POST
    @Path("/processOfllineJob")
    @Produces(MediaType.TEXT_HTML)
    public Viewable processOfflineJob(@FormParam("sourceFile") String src, @FormParam("destinationFile") String dest, @FormParam("maxSpeed") Integer maxSpeed, @FormParam("jobName") String jobName) {
        //get the query parameters

        Job jobObj = null;
        JobWrapper job = null;
        Long jobId = null;
        String message = "";
        try {
            //set the getted query data into the jobModel
            jobObj = new Job();
            jobObj.setSourceFile(src);
            jobObj.setDestniationFile(dest);
            jobObj.setMaxSpeed(maxSpeed);

            if (jobObj != null) {
                //set the job model to the jobWrapper
                job = new JobWrapper();
                job.setJobobj(jobObj);
                job.setJobName(jobName);
                //generate the id of the job 
                job.setJobId(JobFacade.getNextJobId());
                jobId = job.getJobId();

                //insert the name and id in the database
                if (JobFacade.insertJob(job) > 0) {
                    System.out.println(job.getJobName() + " The job is inserted Successfully in the database");
                } else {
                    System.out.println("Failed to insert");
                }
                if (JobFacade.updateJobStatus(Status.NEW, jobId) > 0) {

                    System.out.println(" Job status updated Successfully to new in the database");
                } else {
                    System.out.println("Failed to updated status to new");
                }

                //set the extentsion of the job in the database into HTTP request
                job.setJobExtenstion(ImportMethod.HTTPREQUEST);

                if (JobFacade.updateImportMethod(job.getJobExtenstion(), jobId) > 0) {
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
        HashMap<String, Long> map = new HashMap<>();
        map.put("Job_Id", jobId);

        return new Viewable("/inquireJob", map);
    }

    @GET
    @Path("/processOnlineJob")
    @Produces(MediaType.TEXT_HTML)
    public Viewable ProcessOnlineJob() {
        return new Viewable("/processOnlineJob");
    }

    @POST
    @Path("/processOnlineJob")
    @Produces(MediaType.TEXT_HTML)
    public Viewable ProcessOnlineJob(@FormParam("sourceFile") String src, @FormParam("destinationFile") String dest, @FormParam("maxSpeed") Integer maxSpeed, @FormParam("jobName") String jobName) {
        Job jobObj = new Job();
        JobWrapper job = new JobWrapper();
        Long jobId = null;
        String message = "";

        jobObj.setSourceFile(src);
        jobObj.setDestniationFile(dest);
        jobObj.setMaxSpeed(maxSpeed);

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

            //get the job Object
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        HashMap<String, Long> map = new HashMap<>();
        map.put("Job_Id", jobId);
        return new Viewable("/inquireJob", map);
    }
}
