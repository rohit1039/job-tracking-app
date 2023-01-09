package com.jobify.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserApiResponse
{
    private Integer userId;
    private String firstName;
    private String lastName;
    private String emailID;
    private String password;
    private String location;
    private Integer age;
}
