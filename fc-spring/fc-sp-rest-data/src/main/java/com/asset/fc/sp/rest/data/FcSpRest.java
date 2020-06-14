package com.asset.fc.sp.rest.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.asset.fc")
@EntityScan("com.asset.fc.sp.rest.data.model")
@EnableJpaRepositories("com.asset.fc.sp.rest.data.db.dao")
public class FcSpRest {

    public static void main(String[] args) {
        SpringApplication.run(FcSpRest.class, args);
    }

}
