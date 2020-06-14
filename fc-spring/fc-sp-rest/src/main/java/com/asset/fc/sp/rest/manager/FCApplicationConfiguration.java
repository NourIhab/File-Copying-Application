package com.asset.fc.sp.rest.manager;

import com.asset.fc.sp.common.utility.MyThreadFactory;
import com.asset.fc.sp.rest.property.PropertiesCach;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ArrayBlockingQueue;
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
    private final PropertiesCach prop;
    private String threadName;
    private final MyThreadFactory copier = new MyThreadFactory(threadName);

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Autowired
    public FCApplicationConfiguration(PropertiesCach prop) {
        this.prop = prop;
    }

    @Bean
    public ThreadPoolExecutor copierPool() {
        ArrayBlockingQueue array = new ArrayBlockingQueue<>(capacity);
        setThreadName("copierThread");
        return new ThreadPoolExecutor(prop.getCopierCounter(), prop.getCopierCounter(), 5000, TimeUnit.SECONDS, array, new MyThreadFactory(threadName));
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
