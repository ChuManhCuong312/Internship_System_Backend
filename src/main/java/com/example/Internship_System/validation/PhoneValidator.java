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

        String normalized = phone.trim();

        // Kiểm tra trường hợp bắt đầu bằng 0 nhưng không đủ 10 số
        if (normalized.startsWith("0") && !normalized.startsWith("+")) {
            if (normalized.length() != 10) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "SĐT phải bắt đầu từ 0 và có đủ 10 số"
                ).addConstraintViolation();
                return false;
            }
        }

        // Nếu đã đủ số, kiểm tra đầu số có hợp lệ không
        if (!pattern.matcher(normalized).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Số điện thoại Việt Nam không hợp lệ. " +
                            "Đầu số phải là 03/05/07/08/09. " +
                            "Ví dụ: 0901234567 hoặc +84901234567"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}