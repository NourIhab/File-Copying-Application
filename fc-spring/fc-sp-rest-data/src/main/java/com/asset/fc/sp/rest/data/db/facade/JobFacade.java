package com.asset.fc.sp.rest.data.db.facade;

import com.asset.fc.sp.rest.data.db.service.JobServices;
import com.asset.fc.sp.rest.data.model.JobWrapper;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author nour.ihab
 */
@Component
@Transactional
public class JobFacade {

    private final JobServices service;

    public JobServices getService() {
        return service;
    }

    @Autowired
    public JobFacade(JobServices service) {
        this.service = service;
    }

    public Optional<JobWrapper> inquireJob(Long jobId) {
        return service.inquireJob(jobId);
    }

    public JobWrapper createJobId(JobWrapper job) {
        return service.createJobId(job);

    }
}
