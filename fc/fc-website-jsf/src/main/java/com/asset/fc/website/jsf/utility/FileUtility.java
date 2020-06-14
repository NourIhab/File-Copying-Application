package com.asset.fc.website.jsf.utility;

import com.asset.fc.website.jsf.db.facade.JobFacade;
import com.asset.fc.website.jsf.model.JobWrapper;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@ApplicationScoped
public class FileUtility {

    @ManagedProperty(value = "#{jobFacade}")
    private JobFacade jobFacade;

    public JobFacade getJobFacade() {
        return jobFacade;
    }

    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    public void copyFile(File source, File dest, boolean enableDataBase, JobWrapper job) {
        FileInputStream instream = null;
        FileOutputStream outstream = null;
        float fileSize = 0;
        float totalBytesCopied = 0;
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            instream = new FileInputStream(source);
            outstream = new FileOutputStream(dest);

            if (source.exists()) {
                fileSize = source.length();
                System.out.println("size of the file" + fileSize);
            }

            while ((length = instream.read(buffer)) > 0) {
                if (enableDataBase == true) {
                    outstream.write(buffer, 0, length);
                    totalBytesCopied += length;
                    float currentSpeed = totalBytesCopied / 1024;
                    float FilePercentatgeCopied = 0;
                    float divide = totalBytesCopied / fileSize;
                    float times = divide * ((float) 100);
                    FilePercentatgeCopied = (float) Math.round(times);
                    System.out.println("The copy percentatge = " + FilePercentatgeCopied);

                    if (jobFacade.updateJobSpeedPercentatge(currentSpeed, FilePercentatgeCopied, fileSize, job.getJobId()) > 0) {
                        System.out.println("the Job speed and percentatge are updated Successfully");
                    }

                } else {
                    outstream.write(buffer, 0, length);
                }
            }

        } catch (Exception ex) {
           ex.printStackTrace();
      
        } finally {
            try {
                instream.close();
                outstream.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("File copied successfully!!");
        }
    }
}
