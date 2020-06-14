package com.asset.fc.common.utility;

import com.asset.fc.common.db.facade.JobFacade;
import com.asset.fc.common.model.JobWrapper;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileUtility {

    /*
    //a static function to get the 
    public static void createWriteFile() throws FileNotFoundException, IOException {
        String data = "Hello";
        try (FileOutputStream out = new FileOutputStream("E://FC//TextFile//TextFile.txt")) // create a new file and write in it
        {
            out.write(data.getBytes()); // get the data in the string
        }
    }
     */
 /*
    public static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), new FileOutputStream(dest)); // copy(path file, outputstream file)
        // to path() to get the path of the file
    }
     */
    public static void copyFile(File source, File dest, boolean enableDataBase, JobWrapper job) throws Exception {
        FileInputStream instream = null;
        FileOutputStream outstream = null;
        try {
            instream = new FileInputStream(source);
            outstream = new FileOutputStream(dest);
            float fileSize = 0;
            if (source.exists()) { // if this source file exists
                fileSize = source.length(); // This is the number of bytes we expected to copy..
                System.out.println("size of the file" + fileSize);
            }
            float totalBytesCopied = 0; // This will track the total number of bytes we've copied
            byte[] buffer = new byte[1024];
            int length = 0;
            //float before = System.currentTimeMillis();

            /*copying the contents from input stream to
    	     * output stream using read and write methods
             */
            while ((length = instream.read(buffer)) > 0) {
                if (enableDataBase == true) {

                    //float after = System.currentTimeMillis();
                    outstream.write(buffer, 0, length);
                    totalBytesCopied += length;

                    float currentSpeed = totalBytesCopied / 1024;

                    // System.out.println("Thre current speed = " + currentSpeed);
                    float FilePercentatgeCopied = 0;
                    float divide = totalBytesCopied / fileSize;
                    float times = divide * ((float) 100);
                    FilePercentatgeCopied = (float) Math.round(times);
                    //  FilePercentatgeCopied = (float) Math.round((totalBytesCopied) / (fileSize)) * 100;
                    System.out.println("The copy percentatge = " + FilePercentatgeCopied);
                    if (JobFacade.updateJobSpeedPercentatge(currentSpeed, FilePercentatgeCopied, fileSize, job.getJobId()) > 0) {
                        System.out.println("the Job speed and percentatge are updated Successfully");
                    }

                } else {
                    outstream.write(buffer, 0, length);
                }
            }
        } finally {
            //Closing the input/output file streams
            instream.close();
            outstream.close();

            System.out.println("File copied successfully!!");
        }
    }
}
