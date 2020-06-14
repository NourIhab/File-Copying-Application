package com.asset.fc.website.jsf.manager;

import com.asset.fc.website.jsf.utility.MyThreadFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author nour.ihab
 */
@ManagedBean
@ApplicationScoped
public class WebApplicationManager{

    @ManagedProperty(value = "#{webPropertiesManager}")
    private WebPropertiesManager webPropertiesManager;

    public WebPropertiesManager getWebPropertiesManager() {
        return webPropertiesManager;
    }

    public void setWebPropertiesManager(WebPropertiesManager webPropertiesManager) {
        this.webPropertiesManager = webPropertiesManager;
    }

    MyThreadFactory threadFactory = new MyThreadFactory();
    public ThreadPoolExecutor copier;

    @PostConstruct
    public void Start() {

        try {
            webPropertiesManager.properties();
            // copier = new ThreadPoolExecutor(webPropertiesManager.getCopierCounter(), webPropertiesManager.getCopierCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), threadFactory);
            copier = new ThreadPoolExecutor(webPropertiesManager.getCopierCounter(), webPropertiesManager.getCopierCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), threadFactory);
        } catch (Exception ex) {
        }
    }

    @PreDestroy
    public void Shutdown() {

        try {
            copier.awaitTermination(10, TimeUnit.SECONDS);
            copier.shutdown();
            System.out.println("Copying Thread is Terminating");
        } catch (InterruptedException ex) {
        }
    }

}
