package com.asset.fc.aoe.multiple.threading.manager;

import com.asset.fc.aoe.multiple.threading.utility.MyThreadFactory;
import com.asset.fc.aoe.multiple.threading.thread.JobFetcherThread;
import com.asset.fc.common.manager.ApplicationManager;
import static com.asset.fc.common.parser.ParserFactory.Init;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nour.ihab
 */
public class AOEApplicationManager implements ApplicationManager {

    private final int capacity = 4;

    MyThreadFactory threadFactory = new MyThreadFactory();

    public static ThreadPoolExecutor parser;
    public static ThreadPoolExecutor copier;
    public static ScheduledThreadPoolExecutor fetcher;

    AOEPropertiesManager pme = new AOEPropertiesManager();

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
            copier = new ThreadPoolExecutor(pme.getCopierCounter(), pme.getCopierCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity), threadFactory);
            parser = new ThreadPoolExecutor(pme.getParserCounter(), pme.getParserCounter(), 5000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity), threadFactory);
            fetcher = new ScheduledThreadPoolExecutor(1);
            fetcher.scheduleWithFixedDelay(new JobFetcherThread(pme), 2, 2, TimeUnit.SECONDS);

        } catch (Exception ex) {
            Logger.getLogger(AOEApplicationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Shutdown() {

        try {
            fetcher.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println("Fetching Thread is terminating");
            fetcher.shutdown();

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
