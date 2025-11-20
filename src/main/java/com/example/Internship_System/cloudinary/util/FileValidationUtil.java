package com.example.Internship_System.cloudinary.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileValidationUtil {

    // Maximum file size: 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes

    // Allowed file types for different upload categories
    private static final Set<String> AVATAR_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    ));

    private static final Set<String> CV_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ));

    private static final Set<String> DOCUMENT_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg", "image/png"
    ));

    /**
     * Validate file for avatar upload
     * @param file the file to validate
     * @return validation result with message
     */
    public static FileValidationResult validateAvatarFile(MultipartFile file) {
        return validateFile(file, AVATAR_TYPES, "Avatar");
    }

    /**
     * Validate file for CV upload
     * @param file the file to validate
     * @return validation result with message
     */
    public static FileValidationResult validateCvFile(MultipartFile file) {
        return validateFile(file, CV_TYPES, "CV");
    }

    /**
     * Validate file for document upload (permission, university confirmation)
     * @param file the file to validate
     * @return validation result with message
     */
    public static FileValidationResult validateDocumentFile(MultipartFile file) {
        return validateFile(file, DOCUMENT_TYPES, "Document");
    }

    /**
     * Generic file validation
     * @param file the file to validate
     * @param allowedTypes set of allowed MIME types
     * @param fileCategory category name for error messages
     * @return validation result with message
     */
    private static FileValidationResult validateFile(MultipartFile file, Set<String> allowedTypes, String fileCategory) {
        // Check if file is empty
        if (file == null || file.isEmpty()) {
            return new FileValidationResult(false, "File is empty");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            long sizeMB = file.getSize() / (1024 * 1024);
            return new FileValidationResult(false, 
                    String.format("%s file size (%dMB) exceeds maximum allowed size of 10MB", fileCategory, sizeMB));
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            return new FileValidationResult(false, 
                    String.format("Invalid %s file type. Allowed types: %s", fileCategory.toLowerCase(), allowedTypes));
        }

        return new FileValidationResult(true, "File validation passed");
    }

    /**
     * Inner class to hold validation result
     */
    public static class FileValidationResult {
        private final boolean valid;
        private final String message;

        public FileValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
