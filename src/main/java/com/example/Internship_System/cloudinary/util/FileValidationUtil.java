package com.example.Internship_System.cloudinary.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileValidationUtil {

    // Maximum file size: 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes

    // Allowed file types for different upload categories
    private static final Set<String> AVATAR_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/jpg"
    ));

    private static final Set<String> CV_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ));

    private static final Set<String> DOCUMENT_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    ));

    // Allowed file extensions for document types
    private static final Set<String> DOCUMENT_EXTENSIONS = new HashSet<>(Arrays.asList(
            "pdf", "doc", "docx", "jpg", "jpeg", "png", "gif", "webp"
    ));

    // Magic numbers for file type verification
    private static final byte[] PDF_MAGIC = {0x25, 0x50, 0x44, 0x46}; // %PDF
    private static final byte[] PNG_MAGIC = {(byte) 0x89, 0x50, 0x4E, 0x47}; // .PNG
    private static final byte[] JPEG_MAGIC = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] GIF_MAGIC_87 = {0x47, 0x49, 0x46, 0x38, 0x37, 0x61}; // GIF87a
    private static final byte[] GIF_MAGIC_89 = {0x47, 0x49, 0x46, 0x38, 0x39, 0x61}; // GIF89a
    private static final byte[] DOC_MAGIC = {(byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0}; // MS Office
    private static final byte[] DOCX_MAGIC = {0x50, 0x4B, 0x03, 0x04}; // ZIP (DOCX is ZIP)

    /**
     * Validate file for avatar upload
     * @param file the file to validate
     * @return validation result with message
     */
    public static FileValidationResult validateAvatarFile(MultipartFile file) {
        return validateFile(file, AVATAR_TYPES, "Avatar", false);
    }

    /**
     * Validate file for CV upload
     * @param file the file to validate
     * @return validation result with message
     */
    public static FileValidationResult validateCvFile(MultipartFile file) {
        return validateFile(file, CV_TYPES, "CV", false);
    }

    /**
     * Validate file for document upload (permission, university confirmation)
     * Includes enhanced validation with file extension and magic number checks
     * @param file the file to validate
     * @return validation result with message
     */
    public static FileValidationResult validateDocumentFile(MultipartFile file) {
        FileValidationResult basicValidation = validateFile(file, DOCUMENT_TYPES, "Document", true);

        if (!basicValidation.isValid()) {
            return basicValidation;
        }

        // Additional validation for documents: check file extension
        String filename = file.getOriginalFilename();
        if (filename == null || !hasValidExtension(filename, DOCUMENT_EXTENSIONS)) {
            return new FileValidationResult(false,
                    "Invalid document file extension. Allowed: " + DOCUMENT_EXTENSIONS);
        }

        // Verify file content matches declared type (magic number check)
        try {
            if (!verifyFileContent(file)) {
                return new FileValidationResult(false,
                        "File content does not match the declared file type. Possible security threat.");
            }
        } catch (IOException e) {
            return new FileValidationResult(false,
                    "Error reading file content: " + e.getMessage());
        }

        return new FileValidationResult(true, "Document file validation passed");
    }

    /**
     * Generic file validation
     * @param file the file to validate
     * @param allowedTypes set of allowed MIME types
     * @param fileCategory category name for error messages
     * @param strictValidation whether to perform strict validation
     * @return validation result with message
     */
    private static FileValidationResult validateFile(MultipartFile file, Set<String> allowedTypes,
                                                     String fileCategory, boolean strictValidation) {
        // Check if file is empty
        if (file == null || file.isEmpty()) {
            return new FileValidationResult(false, "File is empty");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            long sizeMB = file.getSize() / (1024 * 1024);
            return new FileValidationResult(false,
                    String.format("%s file size (%dMB) exceeds maximum allowed size of 10MB",
                            fileCategory, sizeMB));
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            return new FileValidationResult(false,
                    String.format("Invalid %s file type. Allowed types: %s",
                            fileCategory.toLowerCase(), allowedTypes));
        }

        // Check filename is not null
        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            return new FileValidationResult(false, "Invalid filename");
        }

        return new FileValidationResult(true, "File validation passed");
    }

    /**
     * Check if filename has valid extension
     */
    private static boolean hasValidExtension(String filename, Set<String> allowedExtensions) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return false;
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        return allowedExtensions.contains(extension);
    }

    /**
     * Verify file content matches declared type using magic numbers
     */
    private static boolean verifyFileContent(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            int bytesRead = is.read(header);

            if (bytesRead < 4) {
                return false;
            }

            String contentType = file.getContentType();
            if (contentType == null) {
                return false;
            }

            // Check based on content type
            switch (contentType.toLowerCase()) {
                case "application/pdf":
                    return startsWith(header, PDF_MAGIC);

                case "image/png":
                    return startsWith(header, PNG_MAGIC);

                case "image/jpeg":
                case "image/jpg":
                    return startsWith(header, JPEG_MAGIC);

                case "image/gif":
                    return startsWith(header, GIF_MAGIC_87) || startsWith(header, GIF_MAGIC_89);

                case "application/msword":
                    return startsWith(header, DOC_MAGIC);

                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    return startsWith(header, DOCX_MAGIC);

                case "image/webp":
                    // WebP: RIFF....WEBP
                    return header[0] == 0x52 && header[1] == 0x49 &&
                            header[2] == 0x46 && header[3] == 0x46;

                default:
                    return true; // Unknown type, pass validation
            }
        }
    }

    /**
     * Check if byte array starts with specific magic number
     */
    private static boolean startsWith(byte[] array, byte[] prefix) {
        if (array.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (array[i] != prefix[i]) {
                return false;
            }
        }
        return true;
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

        @Override
        public String toString() {
            return "FileValidationResult{valid=" + valid + ", message='" + message + "'}";
        }
    }
}