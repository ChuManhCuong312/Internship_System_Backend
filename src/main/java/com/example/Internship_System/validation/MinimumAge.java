package com.example.Internship_System.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinimumAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinimumAge {
    String message() default "Tuổi phải từ 18 trở lên";
    int value() default 18;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}