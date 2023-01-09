package com.jobify.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO
{
    @NotBlank
    @Schema(description = "username of the user, should be in lowercase", required = true, example = "rohitparida0599@gmail.com")
    private String emailID;
    @NotBlank
    @Schema(description = "password of the user", required = true, example = "Rohit@7978")
    private String password;
}
