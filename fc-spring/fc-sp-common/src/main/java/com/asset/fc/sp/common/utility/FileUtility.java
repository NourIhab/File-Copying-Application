package com.asset.fc.sp.common.utility;

import com.asset.fc.sp.common.db.facade.JobFacade;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.common.model.JobWrapper;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileUtility {

    private JobFacade jobFacade;

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
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
                    if (jobFacade.updateJobSpeedPercentatge(currentSpeed, FilePercentatgeCopied, fileSize, job.getJobId()) > 0) {
                        System.out.println("the Job speed and percentatge are updated Successfully");
                    }
                }
            }
        } finally {

            instream.close();
            outstream.close();
            FcLogger.business.info("File copied successfully!!");
        }
    }
}
