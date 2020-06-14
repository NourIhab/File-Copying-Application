package com.asset.fc.servlet.servlet;

import com.asset.fc.common.db.facade.JobFacade;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nour.ihab
 */
public class InquireJob extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jobId = req.getParameter("jobId");
        Long id = Long.parseLong(jobId);

        try {
            resp.getWriter().append("The job Fields of the Current Job ID : " + id + "\n" + JobFacade.getJobFileds(id)).flush();

        } catch (Exception ex) {
            resp.getWriter().append("Failed to retrieve the job fields of this job Id ");
        }
    }

}
