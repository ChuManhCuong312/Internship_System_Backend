package com.example.Internship_System.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    // Regex cho số điện thoại Việt Nam: +84 hoặc 0, sau đó là 3|5|7|8|9 và 8 chữ số
    private static final String PHONE_PATTERN = "^(?:\\+84|0)(?:3|5|7|8|9)\\d{8}$";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.trim().isEmpty()) {
            return true;
        }
        return pattern.matcher(phone.trim()).matches();
    }
}