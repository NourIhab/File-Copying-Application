package com.asset.fc.servlet.manager;

import com.asset.fc.servlet.utility.MyThreadFactory;
//import com.asset.fc.aoe.multiple.threading.thread.JobFetcherThread;
import com.asset.fc.common.manager.ApplicationManager;
import static com.asset.fc.common.parser.ParserFactory.Init;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nour.ihab
 */
public class WebApplicationManager implements ApplicationManager {

    private final int capacity = 4;

    MyThreadFactory threadFactory = new MyThreadFactory();

    public static ThreadPoolExecutor parser;
    public static ThreadPoolExecutor copier;
    //public static ScheduledThreadPoolExecutor fetcher;

    WebPropertiesManager pme = new WebPropertiesManager();

    /**
     *
     */
    @Override
    public void Start() {
        //Intilization Method
        try {
            Init();
            //load the properies file and read the files in it;
            pme.loadProperties("fc.properties");

            //creating the thread pools
            copier = new ThreadPoolExecutor(pme.getCopierCounter(), pme.getCopierCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(capacity), threadFactory);
            parser = new ThreadPoolExecutor(pme.getParserCounter(), pme.getParserCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(capacity), threadFactory);

        } catch (Exception ex) {
            Logger.getLogger(ApplicationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Shutdown() {

        try {

            parser.awaitTermination(10, TimeUnit.SECONDS);
            parser.shutdown();
            System.out.println("Parser Thread is Terminating");

            copier.awaitTermination(10, TimeUnit.SECONDS);
            copier.shutdown();
            System.out.println("Copying Thread is Terminating");

            System.out.println("Termination is done successfully");
        } catch (InterruptedException ex) {
        }
    }

}
