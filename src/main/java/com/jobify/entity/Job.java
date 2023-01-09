package com.jobify.entity;

import com.jobify.entity.enums.JobType;
import com.jobify.entity.enums.Status;
import com.jobify.payload.response.MonthlyAppResponse;
import com.jobify.payload.response.StatsResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "job")
public class Job
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;
    @Column(name = "company", nullable = false)
    private String company;
    @Column(name = "position", nullable = false)
    private String position;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "jobType", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType jobType;
    @Column(name = "jobLocation", nullable = false)
    private String jobLocation;
    @Column(name = "createdBy", nullable = false)
    private String createdBy;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Transient
    private List<StatsResponse> statsResponses;
    @Transient
    private List<MonthlyAppResponse> monthlyAppResponseList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
