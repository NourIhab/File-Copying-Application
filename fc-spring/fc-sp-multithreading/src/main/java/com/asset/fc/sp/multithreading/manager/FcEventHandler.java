package com.asset.fc.sp.multithreading.manager;

import com.asset.fc.sp.multithreading.property.FCPropertiesCach;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.multithreading.task.JobFetcherThread;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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

    private ScheduledThreadPoolExecutor fetcherPool;
    private ThreadPoolExecutor copierPool;
    private ThreadPoolExecutor parserPool;
    private JobFetcherThread fetcher;
    private ApplicationContext context;
    private FCPropertiesCach prop;

    @Autowired
    public void setCopierPool(ThreadPoolExecutor copierPool) {
        this.copierPool = copierPool;
    }

    @Autowired
    public void setParserPool(ThreadPoolExecutor parserPool) {
        this.parserPool = parserPool;
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setFetcherPool(ScheduledThreadPoolExecutor fetcherPool) {
        this.fetcherPool = fetcherPool;
    }

    @Autowired
    public void setFetcher(JobFetcherThread fetcher) {
        this.fetcher = fetcher;
    }

    @Autowired
    public void setProp(FCPropertiesCach prop) {
        this.prop = prop;
    }

    public FcEventHandler() {
    }

    @Override
    public void start(ContextRefreshedEvent start) {
        fetcherPool.scheduleWithFixedDelay(fetcher, 2, 2, TimeUnit.SECONDS);
    }

    @Override
    public void preShutdown() {
        try {
            fetcherPool.awaitTermination(10, TimeUnit.SECONDS);
            fetcherPool.shutdown();
            FcLogger.business.info("Fetcher Thread is Terminating");

            parserPool.awaitTermination(10, TimeUnit.SECONDS);
            parserPool.shutdown();
            FcLogger.business.info("Parser Thread is Terminating");

            copierPool.awaitTermination(10, TimeUnit.SECONDS);
            copierPool.shutdown();
            FcLogger.business.info("Copier Thread is Terminating");

        } catch (InterruptedException ex) {
        }

    }

}
