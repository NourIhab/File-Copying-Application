package com.asset.fc.sp.rest.manager;

import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.rest.property.PropertiesCach;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
public class FcEventHandler extends com.asset.fc.sp.common.handler.FcEventHandler {

    private final ThreadPoolExecutor copierPool;
    private final ApplicationContext context;
    private final PropertiesCach prop;

    @Autowired
    public FcEventHandler(ThreadPoolExecutor copierPool, ApplicationContext context, PropertiesCach prop) {
        this.copierPool = copierPool;
        this.context = context;
        this.prop = prop;
    }

    @Override
    public void start(ContextRefreshedEvent start) {
    }

    @Override
    public void preShutdown() {
        try {

            copierPool.awaitTermination(10, TimeUnit.SECONDS);
            copierPool.shutdown();
            FcLogger.business.info("Copier Thread is Terminating");

        } catch (InterruptedException ex) {
        }

    }

}
