package com.asset.fc.common.parser;

import com.asset.fc.common.model.Job;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.asset.fc.common.annotation.Parser;

@Parser(type = "json")
public class JsonParser implements Parse {

    Job job = null;

    @Override
    public Job Prase(File file) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        //  convert JSON file to Java object
        job = mapper.readValue(file, Job.class);
        System.out.println("----------------------------");
        System.out.println("Source File= " + job.getSourceFile() + " \n" + "Destniation File=  " + job.getDestniationFile() + " \n " + "Max Speed= " + job.getMaxSpeed());
        System.out.println("----------------------------");

        return job;
    }

    @Override
    public Job Prase(String body, String ext) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        //  convert JSON file to Java object
        job = mapper.readValue(body, Job.class);
        System.out.println("----------------------------");
        System.out.println("Source File= " + job.getSourceFile() + " \n" + "Destniation File=  " + job.getDestniationFile() + " \n " + "Max Speed= " + job.getMaxSpeed());
        System.out.println("----------------------------");

        return job;
    }
}
