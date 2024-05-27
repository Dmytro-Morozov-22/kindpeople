package com.dmtrmrzv.kindpeople.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
//    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstName;

    @NotEmpty(message = "Please enter your name")
    private String lastName;

    @NotEmpty(message = "Please enter your name")
    private String username;

    @NotEmpty(message = "Please enter your name")
    @Size(min = 1)
    private String password;
    private String confirmPassword;


}
