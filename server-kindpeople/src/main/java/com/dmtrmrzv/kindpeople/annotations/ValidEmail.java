package com.dmtrmrzv.kindpeople.annotations;

import com.dmtrmrzv.kindpeople.validations.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)

public @interface ValidEmail {
    String message() default "Invalid Email";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}
