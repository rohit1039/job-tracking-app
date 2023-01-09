package com.jobify.service.impl;

import com.jobify.entity.Job;
import com.jobify.entity.User;
import com.jobify.entity.enums.JobType;
import com.jobify.entity.enums.Status;
import com.jobify.exception.ApiException;
import com.jobify.payload.request.JobDTO;
import com.jobify.payload.response.MonthlyAppResponse;
import com.jobify.payload.response.SearchCriteria;
import com.jobify.payload.response.SearchSpecificationForJobs;
import com.jobify.payload.response.StatsResponse;
import com.jobify.repository.JobRepo;
import com.jobify.repository.UserRepo;
import com.jobify.service.JobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class JobServiceImpl implements JobService
{
    private static final Logger LOGGER = LogManager.getLogger(JobServiceImpl.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param jobDTO
     * @param userId
     * @return
     */
    @Override
    public JobDTO createJob(JobDTO jobDTO, Integer userId) throws Exception
    {
        Job job = this.modelMapper.map(jobDTO, Job.class);

        User user = this.userRepo.findById(userId).orElseThrow(() ->
            new UsernameNotFoundException("User with ID: " + userId + " not found to create the job!"));

        job.setCreatedBy(user.getFirstName() + " " + user.getLastName());

        if (isNull(jobDTO.getJobType()))
        {
            job.setJobType(JobType.FULL_TIME);
        } else
        {
            job.setJobType(jobDTO.getJobType());
        }

        if (isNull(jobDTO.getStatus()))
        {
            job.setStatus(Status.PENDING);
        } else
        {
            job.setStatus(jobDTO.getStatus());
        }

        job.setUser(user);

        job.setCreatedAt(LocalDateTime.now());

        Job savedJob = this.jobRepo.save(job);

        return this.modelMapper.map(savedJob, JobDTO.class);

    }

    /**
     * @param jobId
     * @return
     */
    @Override
    public JobDTO getJobById(Integer jobId)
    {
        Job job = this.jobRepo.findById(jobId).orElseThrow(() -> new ApiException("Job not found with ID: " + jobId));

        return this.modelMapper.map(job, JobDTO.class);
    }


    @Override
    public List<JobDTO> getAllJobs(int pageNumber, int pageSize, String sortByJobId, String sortByCompany, String sortByPosition, String sortByJobLocation, String sortDir)
    {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortByJobId, sortByCompany, sortByPosition, sortByJobLocation).ascending() : Sort.by(sortByJobId, sortByCompany, sortByPosition, sortByJobLocation).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        List<Job> jobs = this.jobRepo.findAll(pageable).getContent();

        if (jobs.isEmpty())
        {
            throw new ApiException("No Jobs found");
        }

        Long numOfPages = (long) this.jobRepo.findAll(pageable).getTotalPages();

        Long totalNumberOfJobs = this.jobRepo.findAll(pageable).getTotalElements();

        List<JobDTO> jobDTOs = jobs.stream().map(job ->
            this.modelMapper.map(job, JobDTO.class)).toList();

        jobDTOs = jobDTOs.stream().peek(j ->
        {
            j.setNumberOfPages(numOfPages);
            j.setTotalNumberOfJobs(totalNumberOfJobs);
        }).toList();

        return jobDTOs;
    }

    /**
     * @param searchVal
     * @return
     */
    public List<JobDTO> searchByAllFields(String searchVal)
    {
        SearchSpecificationForJobs spec1 =
            new SearchSpecificationForJobs(new SearchCriteria("company", ":", searchVal));

        SearchSpecificationForJobs spec2 =
            new SearchSpecificationForJobs(new SearchCriteria("jobLocation", ":", searchVal));

        SearchSpecificationForJobs spec3 =
            new SearchSpecificationForJobs(new SearchCriteria("position", ":", searchVal));

        List<Job> results =
            jobRepo.findAll(Specification.where(spec1).or(spec2).or(spec3));

        return results.stream().map(j -> this.modelMapper.map(j, JobDTO.class)).toList();
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<JobDTO> getJobsByUserWithStats(Integer userId)
    {

        User user = this.userRepo.findById(userId).orElseThrow(() ->
            new UsernameNotFoundException("User with ID: " + userId + " not found!"));

        List<Job> jobs = this.jobRepo.findByUser(user);

        if (jobs.isEmpty())
        {
            throw new ApiException("No jobs created by user: " + user.getEmailID());
        }

        String createdBy = jobs.stream().map(Job::getCreatedBy).findAny().map(Object::toString).orElse("");

        List<StatsResponse> statsResponses = this.jobRepo.getCountByStatus(createdBy);

        statsResponses = statsResponses.stream().peek(s ->
            s.setCreatedBy(createdBy)).toList();

        List<StatsResponse> finalStatsResponses = statsResponses;
        List<JobDTO> jobDTOs = jobs.stream().map(job ->
        {
            job.setStatsResponses(finalStatsResponses);
            return this.modelMapper.map(job, JobDTO.class);

        }).toList();

        return jobDTOs;
    }

    @Override
    public List<JobDTO> getJobsByUserWithMonthlyStats(Integer userId)
    {
        User user = this.userRepo.findById(userId).orElseThrow(() ->
            new UsernameNotFoundException("User with ID: " + userId + " not found!"));

        List<Job> jobs = this.jobRepo.findByUser(user);

        if (jobs.isEmpty())
        {
            throw new ApiException("No jobs created by user: " + user.getEmailID());
        }

        String createdBy = jobs.stream().map(Job::getCreatedBy).findAny().map(Object::toString).orElse("");

        List<MonthlyAppResponse> monthlyAppResponseList = this.jobRepo.getCountByCreatedAt(createdBy);

        List<JobDTO> jobDTOList = jobs.stream().map(j ->
        {
            monthlyAppResponseList.stream().map(m -> Integer.parseInt(m.getCreatedAt().getMonth().toString()) - 1);
            j.setMonthlyAppResponseList(monthlyAppResponseList);

            return this.modelMapper.map(j, JobDTO.class);
        }).collect(Collectors.toList());

        return jobDTOList;
    }

    /**
     * @param jobDTO
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public JobDTO updateJob(JobDTO jobDTO, Integer jobId) throws Exception
    {
        JobDTO updatedJob;

        try
        {
            Job job = this.jobRepo.findById(jobId).orElseThrow(() -> new ApiException("Job not found with ID: " + jobId));

            if (isNull(jobDTO.getJobType()))
            {
                job.setJobType(JobType.FULL_TIME);
            } else
            {
                job.setJobType(jobDTO.getJobType());
            }

            if (isNull(jobDTO.getStatus()))
            {
                job.setStatus(Status.PENDING);
            } else
            {
                job.setStatus(jobDTO.getStatus());
            }

            job.setPosition(jobDTO.getPosition());
            job.setCompany(jobDTO.getCompany());
            job.setJobLocation(jobDTO.getJobLocation());
            job.setCreatedBy(jobDTO.getCreatedBy());
            job.setCreatedAt(LocalDateTime.now());

            Job updated = this.jobRepo.save(job);

            updatedJob = this.modelMapper.map(updated, JobDTO.class);
        } catch (Exception e)
        {
            LOGGER.error("{}", e.getLocalizedMessage());
            throw new Exception(e.getLocalizedMessage());
        }

        LOGGER.info("Job updated successfully with ID: " + jobId);

        return updatedJob;
    }

    /**
     * @param jobId
     * @return
     */
    @Override
    public JobDTO deleteJob(Integer jobId)
    {
        Job job = this.jobRepo.findById(jobId).orElseThrow(() -> new ApiException("Job not found with ID: " + jobId));

        this.jobRepo.delete(job);

        JobDTO jobDTO = this.modelMapper.map(job, JobDTO.class);

        LOGGER.info("Job deleted successfully with ID: " + jobId);

        return jobDTO;
    }
}
