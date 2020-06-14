package com.asset.fc.aoe.db.manager;

import com.asset.fc.aoe.db.thread.CopyingThread;
import com.asset.fc.aoe.db.thread.JobFetcherThread;
import com.asset.fc.aoe.db.thread.ParserThread;
import com.asset.fc.common.manager.ApplicationManager;
import com.asset.fc.common.model.JobWrapper;
import static com.asset.fc.common.parser.ParserFactory.Init;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nour.ihab
 */
public class AOE_ApplicationManager implements ApplicationManager {

    private final int capacity = 4;
    private final ArrayBlockingQueue<JobWrapper> ParserQueue = new ArrayBlockingQueue<>(capacity);
    private final ArrayBlockingQueue<JobWrapper> CopyQueue = new ArrayBlockingQueue<>(capacity);
    AtomicBoolean shutDownSignle = new AtomicBoolean(true);
    CopyingThread copying[];
    ParserThread parsing[];
    JobFetcherThread Fetching;
    AOE_PropertiesManager pme = new AOE_PropertiesManager();

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
            // DataBaseManager.getInstance().dbIntilization("db.properties");

            //Creating an array of Threads
            copying = new CopyingThread[pme.getCopierCounter()];
            parsing = new ParserThread[pme.getParserCounter()];
            Fetching = new JobFetcherThread(pme, ParserQueue, shutDownSignle);

            //Start the threads
            for (int i = 0; i < pme.getCopierCounter(); i++) {
                copying[i] = new CopyingThread(CopyQueue, shutDownSignle);
                copying[i].start();

            }

            for (int j = 0; j < pme.getParserCounter(); j++) {
                parsing[j] = new ParserThread(ParserQueue, shutDownSignle, CopyQueue);
                parsing[j].start();

            }

            Fetching.start();

            //Register the shutdown Thread to shutdown the application
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown Hook is running!");

                Shutdown();

            }));

        } catch (Exception ex) {
            Logger.getLogger(AOE_ApplicationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Shutdown() {

        try {

            //change the flag to false
            shutDownSignle.set(false);

            //shutdown threads
            Fetching.join();
            System.out.println("Fetching Thread is terminating");

            for (int j = 0; j < pme.getParserCounter(); j++) {
                parsing[j].join();
            }
            System.out.println("Parser Thread is Terminating");

            for (int i = 0; i < pme.getCopierCounter(); i++) {
                copying[i].join();
            }
            System.out.println("Copying Thread is Terminating");

            System.out.println("Termination is done successfully");
        } catch (InterruptedException ex) {
        }
    }

}
