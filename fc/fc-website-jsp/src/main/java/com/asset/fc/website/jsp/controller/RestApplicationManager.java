package com.asset.fc.website.jsp.controller;

import com.asset.fc.website.jsp.manager.WebApplicationManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author nour.ihab
 */
@WebListener
public class RestApplicationManager implements ServletContextListener {

    WebApplicationManager ap;

    public RestApplicationManager() {
        this.ap = new WebApplicationManager();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ap.Start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ap.Shutdown();
    }

}
