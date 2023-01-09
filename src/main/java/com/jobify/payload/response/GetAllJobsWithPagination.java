package com.jobify.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllJobsWithPagination
{
    private List<JobApiResponse> jobs;
    private Long numberOfPages;
    private Long numberOfJobs;
    private Long totalNumberOfJobs;
}
