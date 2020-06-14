package com.asset.fc.sp.multithreading.manager;

import com.asset.fc.sp.multithreading.property.FCPropertiesCach;
import com.asset.fc.sp.common.utility.MyThreadFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author nour.ihab
 */
@SpringBootConfiguration
public class FCApplicationConfiguration {

    private final int capacity = 4;
    private final FCPropertiesCach prop;
    private String threadName;
    private final MyThreadFactory copier = new MyThreadFactory(threadName);
    private final MyThreadFactory parser = new MyThreadFactory(threadName);

    @Autowired
    public FCApplicationConfiguration(FCPropertiesCach prop) {
        this.prop = prop;
    }

    @Bean
    public ThreadPoolExecutor parserPool() {
        return new ThreadPoolExecutor(prop.getParserCounter(), prop.getParserCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity), parser);
    }

    @Bean
    public ThreadPoolExecutor copierPool() {
        return new ThreadPoolExecutor(prop.getCopierCounter(), prop.getCopierCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity), copier);

    }

    @Bean
    public ScheduledThreadPoolExecutor fetcherPool() {
        return new ScheduledThreadPoolExecutor(1);
    }

    @Bean
    @ConfigurationProperties(prefix = "fc-sp-datasource")
    public HikariConfig FcHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Autowired
    public HikariDataSource FcHikariDataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

}
