package com.jobify.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse
{
    private Integer userId;
    private String token;
    private String emailID;
    private String fullName;
    private String firstName;
    private String lastName;
    private Integer age;
    private String location;
}
