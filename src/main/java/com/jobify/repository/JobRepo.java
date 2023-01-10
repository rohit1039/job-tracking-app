package com.jobify.repository;

import com.jobify.entity.Job;
import com.jobify.entity.User;
import com.jobify.payload.response.MonthlyAppResponse;
import com.jobify.payload.response.StatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job>
{
    List<Job> findByUser(User user);

    @Query("SELECT " +
           "new com.jobify.payload.response.StatsResponse(status, COUNT(*), createdBy) " +
           "FROM " +
           "Job  " + " where createdBy = :createdBy " +
           "GROUP BY " +
           "status")
    List<StatsResponse> getCountByStatus(@Param("createdBy") String createdBy);


    /**
     * @param createdBy
     * @return NOTE: limit keyword is not supported in JPQL
     */
    @Query(value = "SELECT " +
                   "new com.jobify.payload.response.MonthlyAppResponse(COUNT(*), createdAt, year(createdAt), month(createdAt), createdBy ) " +
                   "FROM " +
                   "Job  " + " where createdBy = :createdBy " +
                   "GROUP BY " +
                   "year(createdAt), month(createdAt) " + " ORDER BY year(createdAt) DESC, month(createdAt) DESC ")
    List<MonthlyAppResponse> getCountByCreatedAt(@Param("createdBy") String createdBy);

}
