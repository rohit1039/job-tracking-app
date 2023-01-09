package com.jobify.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jobify.entity.enums.JobType;
import com.jobify.entity.enums.Status;
import com.jobify.payload.response.MonthlyAppResponse;
import com.jobify.payload.response.StatsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO
{
    @JsonIgnore
    private Integer jobId;
    @NotBlank
    @Schema(description = "name of the company", required = true, example = "Facebook")
    private String company;
    @NotBlank
    @Schema(description = "position cannot be empty", required = true, example = "Software Engineer")
    private String position;
    @Schema(description = "status cannot be empty", required = false, example = "PENDING")
    private Status status;
    @Schema(description = "jobType cannot be empty", required = false, example = "FULL_TIME")
    private JobType jobType;
    @NotBlank
    @Schema(description = "jobLocation cannot be empty", required = true, example = "Bangalore")
    private String jobLocation;
    @JsonIgnore
    private String createdBy;
    private LocalDateTime createdAt;
    private Long numberOfPages;
    private Long totalNumberOfJobs;
    private List<StatsResponse> statsResponses;
    private List<MonthlyAppResponse> monthlyAppResponseList;
}
