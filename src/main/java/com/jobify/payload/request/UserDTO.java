package com.jobify.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO
{
    @JsonIgnore
    private Integer userID;
    @NotBlank
    @Schema(description = "first name of the user", required = true, example = "Rohit")
    private String firstName;
    @NotBlank
    @Schema(description = "last name of the user", required = true, example = "Parida")
    private String lastName;
    @NotBlank
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Schema(description = "username of the user, should be in lowercase", required = true, example = "rohitparida0599@gmail.com")
    private String emailID;
    @NotBlank
    @Size(min = 8, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$",
        message = "minimum 1 uppercase letter, " + "minimum 1 lowercase letter, " + "minimum 1 special character, " +
                  "minimum 1 number, " + "minimum 8 characters ")
    @Schema(description = "password of the user", required = true, example = "Rohit@123")
    private String password;
    @NotBlank
    @Schema(description = "location of the user", required = true, example = "Bangalore")
    private String location;
    @NotNull
    @PositiveOrZero
    @Schema(description = "age of the user", required = true, example = "25")
    private Integer age;
    private Long numberOfPages;
    private Long totalNumberOfUsers;
    private List<JobDTO> jobDTO;
}
