package com.asset.fc.aoe.engine.thread;

import com.asset.fc.common.model.Job;
import com.asset.fc.common.parser.ParserFactory;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author nour.ihab
 */
public class ParserThread extends Thread {

    //we used the arrayblockingqueue to protect our resource to be accessed by one thread only and cannot be shared by another thread automatically
    ArrayBlockingQueue<File> ParserQueue;
    ArrayBlockingQueue<Job> CopyQueue;
    AtomicBoolean shutDownSignle;

    public ParserThread(ArrayBlockingQueue<File> ParserQueue, AtomicBoolean shutDownSignle, ArrayBlockingQueue<Job> CopyQueue) {
        this.shutDownSignle = shutDownSignle;
        this.ParserQueue = ParserQueue;
        this.CopyQueue = CopyQueue;
    }

    @Override
    public void run() {
        try {
            while (shutDownSignle.get() == true || !ParserQueue.isEmpty()) {
                File parserOutPut = ParserQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (parserOutPut == null) {
                    continue;
                } else {
                    CopyQueue.put(ParserFactory.Parse(parserOutPut));
                    System.out.println("Files are parsed successfully in the Copy Queue: " + CopyQueue);
                }
            }
        } catch (Exception e) {
            // Throwing an exception 
            System.out.println("Exception is caught Parser Thread");
        }
    }
}
