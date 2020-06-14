package com.asset.fc.sp.rest.data.manager;

import com.asset.fc.sp.rest.data.logger.FcLogger;
import com.asset.fc.sp.rest.data.property.PropertiesCach;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FcEventHandler {

    private final ThreadPoolExecutor copierPool;
    private final ApplicationContext context;
    private final PropertiesCach prop;

    @Autowired
    public FcEventHandler(ThreadPoolExecutor copierPool, ApplicationContext context, PropertiesCach prop) {
        this.copierPool = copierPool;
        this.context = context;
        
        this.prop = prop;
    }

    @EventListener
    public void start(ContextRefreshedEvent start) {
    }

    @EventListener
    public final void Shutdown(ContextClosedEvent close) {
        preShutdown();
    }

    public void preShutdown() {
        try {

            copierPool.awaitTermination(10, TimeUnit.SECONDS);
            copierPool.shutdown();
            FcLogger.business.info("Copier Thread is Terminating");

        } catch (InterruptedException ex) {
        }

    }

}
