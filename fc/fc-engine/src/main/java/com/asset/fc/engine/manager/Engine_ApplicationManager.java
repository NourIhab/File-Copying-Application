package com.asset.fc.engine.manager;

import com.asset.fc.common.manager.ApplicationManager;
import com.asset.fc.common.model.Job;
import com.asset.fc.common.parser.ParserFactory;
import static com.asset.fc.common.parser.ParserFactory.Init;
import static com.asset.fc.common.utility.FileUtility.copyFile;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author nour.ihab
 */
public class Engine_ApplicationManager implements ApplicationManager {

    @Override
    public void Start() {
        //Intilization Method
        try {
            Init();
        } catch (Exception ex) {
        }
        try {

            Engine_PropertiesManager pme = new Engine_PropertiesManager();
            pme.loadProperties("fc.properties");

            //Open job-folder and loop over files in it
            File jobFolder = new File(pme.getJobFolder());
            File[] files = jobFolder.listFiles();

            for (File f : files) {
                //Send every file to parser and get job content
                Job jobobj = ParserFactory.Parse(f);
                //perform job using the file utility
                System.out.println(f.getName());
                //change the string value into file value
                File file = new File(jobobj.getSourceFile());
                File file2 = new File(jobobj.getDestniationFile());
                boolean enableDataBase = false;
                copyFile(file, file2, enableDataBase, null);// copy the files
                System.out.println("The file is copied successfuly");
            }
        } catch (IOException e) {
        } catch (Exception ex) {
        }

    }

}
