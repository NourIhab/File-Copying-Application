package com.asset.fc.sp.multithreading.task;

import com.asset.fc.sp.common.model.JobWrapper;
import com.asset.fc.sp.common.constant.ImportMethod;
import com.asset.fc.sp.common.constant.Status;
import com.asset.fc.sp.common.db.facade.JobFacade;
import com.asset.fc.sp.common.logger.FcLogger;
import com.asset.fc.sp.multithreading.property.FCPropertiesCach;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
@Scope("prototype")
public class JobFetcherThread implements Runnable {

    private final FCPropertiesCach prop;
    private ObjectMapper mapper;
    private JobFacade jobFacade;
    private ThreadPoolExecutor parserPool;
    private ParserThread parser;
    private ApplicationContext context;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setParserPool(ThreadPoolExecutor parserPool) {
        this.parserPool = parserPool;
    }

    @Autowired

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setParser(ParserThread parser) {
        this.parser = parser;
    }

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }

    @Autowired
    public JobFetcherThread(FCPropertiesCach prop) {
        this.prop = prop;
    }

    public FCPropertiesCach getProp() {
        return prop;
    }

    List<String> list = new ArrayList<String>();

    @Override
    public void run() {
        JobWrapper job = null;
        try {
            File jobFolder = new File(prop.getJobFolder());
            File[] files = jobFolder.listFiles();
            for (File f : files) {
                if ((list.contains(f.getName()))) {
                } else {
                    job = new JobWrapper();
                    job.setJobId(jobFacade.getNextJobId());
                    job.setJobName(f.getName());
                    job.setFile(f);

                    if (jobFacade.insertJob(job) > 0) {
                        FcLogger.business.info(job.getJobName() + " Is inserted Successfully in the database");
                    } else {
                        FcLogger.business.info("Failed to insert");
                    }
                    if (jobFacade.updateJobStatus(Status.NEW, job.getJobId()) > 0) {

                        FcLogger.business.info(" Job status updated Successfully to new in the database");
                    } else {
                        FcLogger.business.info("Failed to updated status to new");
                    }
                    String fileExtention = job.getFile().getName().substring(job.getFile().getName().lastIndexOf(".") + 1);
                    if ("json".equals(fileExtention)) {
                        job.setJobExtenstion(ImportMethod.valueOf(fileExtention.toUpperCase()));

                        if (jobFacade.updateImportMethod(job.getJobExtenstion(), job.getJobId()) > 0) {
                            FcLogger.business.info("Import Method Updated Successfully in the database");
                        } else {
                            System.out.println("Failed to updated the import Method");
                        }

                        parserPool.execute(context.getBean(ParserThread.class, job));
                        FcLogger.business.info("--------------------------------------------------------------");
                        list.add(f.getName());
                    } else {
                        File file = job.getFile();
                        throw new Exception("This extention is not supported" + file.getName().substring(file.getName().lastIndexOf(".") + 1));
                    }
                }
            }

        } catch (Exception ex) {
            String failedReson = ex.getMessage();
            try {
                jobFacade.updateStatusFailedReson(job.getJobId(), Status.FAILED, failedReson);
            } catch (Exception ex1) {
                ex.printStackTrace();
            }

        }

    }
}
