package com.asset.fc.sp.rest.data.db.service;

import com.asset.fc.sp.rest.data.db.dao.JobDao;
import com.asset.fc.sp.rest.data.model.JobWrapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
public class JobServices {

    private final JobDao dao;

    public JobDao getDao() {
        return dao;
    }

    @Autowired
    public JobServices(JobDao dao) {
        this.dao = dao;
    }

    public Optional<JobWrapper> inquireJob(Long jobId) {
        return dao.findById(jobId);
    }

    public JobWrapper createJobId(JobWrapper job) {
        return dao.saveAndFlush(job);
    }
   

}
