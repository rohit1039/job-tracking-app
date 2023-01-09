package com.jobify.service;

import com.jobify.payload.request.JobDTO;

import java.util.List;

public interface JobService
{
    JobDTO createJob(JobDTO jobDTO, Integer userId) throws Exception;

    JobDTO getJobById(Integer jobId);

    List<JobDTO> getAllJobs(int pageNumber, int pageSize, String sortByJobId, String sortByCompany, String sortByPosition, String sortByJobLocation, String sortDir);

    List<JobDTO> searchByAllFields(String searchVal);

    List<JobDTO> getJobsByUserWithStats(Integer userId);

    List<JobDTO> getJobsByUserWithMonthlyStats(Integer userId);

    JobDTO updateJob(JobDTO jobDTO, Integer jobId) throws Exception;

    JobDTO deleteJob(Integer jobId);
}
