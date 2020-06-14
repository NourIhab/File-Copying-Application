package com.asset.fc.sp.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.asset.fc")
public class FcSpRest {

    public static void main(String[] args) {
        SpringApplication.run(FcSpRest.class, args);
        
    }

}
