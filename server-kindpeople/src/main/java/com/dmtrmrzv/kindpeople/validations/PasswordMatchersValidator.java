package com.dmtrmrzv.kindpeople.validations;

import com.dmtrmrzv.kindpeople.annotations.PasswordMatchers;
import com.dmtrmrzv.kindpeople.payload.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchersValidator implements ConstraintValidator<PasswordMatchers, Object> {
    @Override
    public void initialize(PasswordMatchers constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) object;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }
}
