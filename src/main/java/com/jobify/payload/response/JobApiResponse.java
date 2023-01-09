package com.jobify.payload.response;

import com.jobify.entity.enums.JobType;
import com.jobify.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApiResponse
{
    private Integer jobId;
    private String company;
    private String position;
    private Status status;
    private JobType jobType;
    private String jobLocation;
    private String createdBy;
    private LocalDateTime createdAt;
}
