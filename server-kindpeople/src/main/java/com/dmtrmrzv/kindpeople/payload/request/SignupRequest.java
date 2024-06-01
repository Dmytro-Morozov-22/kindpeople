package com.dmtrmrzv.kindpeople.payload.request;

import com.dmtrmrzv.kindpeople.annotations.PasswordMatchers;
import com.dmtrmrzv.kindpeople.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatchers
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your firstName")
    private String firstname;

    @NotEmpty(message = "Please enter your lastName")
    private String lastname;

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Please enter your password")
    @Size(min = 1)
    private String password;
    private String confirmPassword;


}
