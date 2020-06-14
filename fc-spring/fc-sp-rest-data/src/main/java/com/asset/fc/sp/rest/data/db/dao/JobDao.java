package com.asset.fc.sp.rest.data.db.dao;

import com.asset.fc.sp.rest.data.model.JobWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nour.ihab
 */
@Repository
public interface JobDao extends JpaRepository<JobWrapper, Long> {

}
