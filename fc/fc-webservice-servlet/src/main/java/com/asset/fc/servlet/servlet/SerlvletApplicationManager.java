package com.asset.fc.servlet.servlet;

import com.asset.fc.servlet.manager.WebApplicationManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author nour.ihab
 */
public class SerlvletApplicationManager extends HttpServlet {

    static WebApplicationManager ap = new WebApplicationManager();

    @Override
    public void init() throws ServletException {

        ap.Start();
    }

    @Override
    public void destroy() {
        ap.Shutdown();

    }

}
