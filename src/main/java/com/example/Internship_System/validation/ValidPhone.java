package com.example.Internship_System.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}