package com.asset.fc.sp.rest.data.utlity;

import com.asset.fc.sp.rest.data.db.facade.JobFacade;
import com.asset.fc.sp.rest.data.logger.FcLogger;
import com.asset.fc.sp.rest.data.model.JobWrapper;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileUtility {

    private JobWrapper job;
    private JobFacade facade;

    public void setJob(JobWrapper job) {
        this.job = job;
    }


    @Autowired
    public void setJobFacade(JobFacade facade) {
        this.facade = facade;
    }

    public void copyFile(File source, File dest, boolean enableDataBase, JobWrapper job) throws Exception {
        FileInputStream instream = null;
        FileOutputStream outstream = null;
        try {
            instream = new FileInputStream(source);
            outstream = new FileOutputStream(dest);
            float fileSize = 0;
            if (source.exists()) {
                fileSize = source.length();
                System.out.println("size of the file" + fileSize);
            }
            float totalBytesCopied = 0;
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = instream.read(buffer)) > 0) {
                if (enableDataBase == true) {

                    outstream.write(buffer, 0, length);
                    totalBytesCopied += length;

                    float currentSpeed = totalBytesCopied / 1024;

                    float FilePercentatgeCopied = 0;
                    float divide = totalBytesCopied / fileSize;
                    float times = divide * ((float) 100);
                    FilePercentatgeCopied = (float) Math.round(times);

                    FcLogger.business.info("The copy percentatge = " + FilePercentatgeCopied);
                    job.setCopyPercentage(FilePercentatgeCopied);
                    job.setFileSize(fileSize);
                    job.setCurrentSpeed(currentSpeed);
                    facade.createJobId(job);
                }
            }
        } finally {

            instream.close();
            outstream.close();
            FcLogger.business.info("File copied successfully!!");
        }
    }
}
