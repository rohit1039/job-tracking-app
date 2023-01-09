package com.jobify.payload.response;

import com.jobify.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse
{
    private Status status;
    private Long count;
    private String createdBy;
}
