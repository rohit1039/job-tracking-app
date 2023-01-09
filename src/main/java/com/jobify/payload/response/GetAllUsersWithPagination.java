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
public class GetAllUsersWithPagination
{
    private List<UserApiResponse> users;
    private Long numberOfPages;
    private Long numberOfUsers;
    private Long totalNumberOfUsers;
}
