package com.jobify.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAppResponse
{
    private Long count;
    private LocalDateTime createdAt;
    private Integer year;
    private Integer month;
    private String createdBy;
}
