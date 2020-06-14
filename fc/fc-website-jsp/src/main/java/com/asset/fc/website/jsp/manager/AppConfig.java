package com.asset.fc.website.jsp.manager;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

/**
 *
 * @author nour.ihab
 */
@ApplicationPath("/jsp")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        packages("com.asset.fc.website.jsp.controller");
        property(JspMvcFeature.TEMPLATE_BASE_PATH, "/");
        register(JspMvcFeature.class);
    }
}
