package com.asset.fc.common.parser;

import com.asset.fc.common.model.Job;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import com.asset.fc.common.annotation.Parser;

@Parser(type = "csv")
public class CsvParser implements Parse {

    Job job = null;

    @Override
    public Job Prase(File file) throws Exception {
        job = new Job();
        String line = "";
        String cvsSplitBy = ",";
        // read the csv file
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] job2 = line.split(cvsSplitBy);
            //set the values of the files
            job.setSourceFile(job2[0]);
            job.setDestniationFile(job2[1]);
            job.setMaxSpeed(Integer.parseInt(job2[2]));
            System.out.println("--------------------------------------------------------------");
            // output the file on the console
            System.out.println("Job [Source File= " + job2[0] + "\n" + " Desniation File= " + job2[1] + "\n" + " Max Speed= " + job2[2] + "");
            System.out.println("--------------------------------------------------------------");

        }

        return job;
    }

    @Override
    public Job Prase(String body, String ext) throws Exception {

        return job;
    }
}
