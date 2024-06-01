package com.dmtrmrzv.kindpeople.annotations;

import com.dmtrmrzv.kindpeople.validations.PasswordMatchersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchersValidator.class)
@Documented
public @interface PasswordMatchers {
    String message() default "Passwords do not match";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}
