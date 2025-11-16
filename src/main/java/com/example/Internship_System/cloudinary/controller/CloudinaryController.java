package com.example.Internship_System.cloudinary.controller;

import com.example.Internship_System.cloudinary.service.CloudinaryService;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.intern.service.InternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cloudinary")
@CrossOrigin(origins = "*")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private InternService internService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.upload(file);

            Map<String, Object> response = new HashMap<>();
            response.put("url", uploadResult.get("url"));
            response.put("publicId", uploadResult.get("public_id"));
            response.put("message", "Image uploaded successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @PostMapping("/upload/{folder}")
    public ResponseEntity<Map<String, Object>> uploadImageToFolder(
            @RequestParam("file") MultipartFile file,
            @PathVariable String folder) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.upload(file, folder);

            Map<String, Object> response = new HashMap<>();
            response.put("url", uploadResult.get("url"));
            response.put("publicId", uploadResult.get("public_id"));
            response.put("folder", folder);
            response.put("message", "Image uploaded successfully to folder");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<Map<String, Object>> deleteImage(
            @PathVariable String publicId) {
        try {
            Map<String, Object> deleteResult = cloudinaryService.delete(publicId);

            Map<String, Object> response = new HashMap<>();
            response.put("result", deleteResult.get("result"));
            response.put("message", "Image deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/url/{publicId}")
    public ResponseEntity<Map<String, String>> getImageUrl(
            @PathVariable String publicId,
            @RequestParam(required = false) Integer width,
            @RequestParam(required = false) Integer height) {
        try {
            String url;
            if (width != null && height != null) {
                url = cloudinaryService.getImageUrl(publicId, width, height);
            } else {
                url = cloudinaryService.getImageUrl(publicId);
            }

            Map<String, String> response = new HashMap<>();
            response.put("url", url);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // ============================================
    // AVATAR UPLOAD & RETRIEVAL
    // ============================================

    @PostMapping(value = "/upload/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "internId", required = false) Integer internId) {
        try {
            // Upload to avatars folder
            Map<String, Object> uploadResult = cloudinaryService.upload(file, "avatars");
            String fileUrl = (String) uploadResult.get("url");

            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("publicId", uploadResult.get("public_id"));
            response.put("message", "Avatar uploaded successfully");

            // If internId is provided, update the InternProfile
            if (internId != null) {
                Optional<InternProfile> profileOpt = internService.findById(internId);
                if (profileOpt.isPresent()) {
                    InternProfile profile = profileOpt.get();
                    // Delete old avatar if exists
                    if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                        try {
                            // Extract publicId from URL or use the stored value
                            String oldPublicId = extractPublicIdFromUrl(profile.getAvatar());
                            if (oldPublicId != null) {
                                cloudinaryService.delete(oldPublicId);
                            }
                        } catch (Exception e) {
                            // Log but don't fail if old file deletion fails
                            System.err.println("Failed to delete old avatar: " + e.getMessage());
                        }
                    }
                    profile.setAvatar(fileUrl);
                    internService.save(profile);
                    response.put("internId", internId);
                    response.put("updated", true);
                } else {
                    response.put("warning", "Intern profile not found for internId: " + internId);
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/avatar/{internId}")
    public ResponseEntity<Map<String, Object>> getAvatar(@PathVariable("internId") int internId) {
        try {
            Optional<InternProfile> profileOpt = internService.findById(internId);
            if (profileOpt.isPresent()) {
                InternProfile profile = profileOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("internId", internId);
                response.put("avatarUrl", profile.getAvatar() != null ? profile.getAvatar() : null);
                response.put("hasAvatar", profile.getAvatar() != null && !profile.getAvatar().isEmpty());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Intern profile not found for internId: " + internId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // ============================================
    // CV FILE UPLOAD & RETRIEVAL
    // ============================================

    @PostMapping(value = "/upload/cv", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadCvFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "internId", required = false) Integer internId) {
        try {
            // Upload to cv_files folder
            Map<String, Object> uploadResult = cloudinaryService.upload(file, "cv_files");
            String fileUrl = (String) uploadResult.get("url");

            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("publicId", uploadResult.get("public_id"));
            response.put("message", "CV file uploaded successfully");

            // If internId is provided, update the InternProfile
            if (internId != null) {
                Optional<InternProfile> profileOpt = internService.findById(internId);
                if (profileOpt.isPresent()) {
                    InternProfile profile = profileOpt.get();
                    // Delete old CV file if exists
                    if (profile.getCvFile() != null && !profile.getCvFile().isEmpty()) {
                        try {
                            String oldPublicId = extractPublicIdFromUrl(profile.getCvFile());
                            if (oldPublicId != null) {
                                cloudinaryService.delete(oldPublicId);
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to delete old CV file: " + e.getMessage());
                        }
                    }
                    profile.setCvFile(fileUrl);
                    internService.save(profile);
                    response.put("internId", internId);
                    response.put("updated", true);
                } else {
                    response.put("warning", "Intern profile not found for internId: " + internId);
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/cv/{internId}")
    public ResponseEntity<Map<String, Object>> getCvFile(@PathVariable("internId") int internId) {
        try {
            Optional<InternProfile> profileOpt = internService.findById(internId);
            if (profileOpt.isPresent()) {
                InternProfile profile = profileOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("internId", internId);
                response.put("cvFileUrl", profile.getCvFile() != null ? profile.getCvFile() : null);
                response.put("hasCvFile", profile.getCvFile() != null && !profile.getCvFile().isEmpty());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Intern profile not found for internId: " + internId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // ============================================
    // PERMISSION FILE UPLOAD & RETRIEVAL
    // ============================================

    @PostMapping(value = "/upload/permission", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> uploadPermissionFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "internId", required = false) Integer internId) {
        try {
            // Upload to permission_files folder
            Map<String, Object> uploadResult = cloudinaryService.upload(file, "permission_files");
            String fileUrl = (String) uploadResult.get("url");

            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("publicId", uploadResult.get("public_id"));
            response.put("message", "Permission file uploaded successfully");

            // If internId is provided, update the InternProfile
            if (internId != null) {
                Optional<InternProfile> profileOpt = internService.findById(internId);
                if (profileOpt.isPresent()) {
                    InternProfile profile = profileOpt.get();
                    // Delete old permission file if exists
                    if (profile.getPermissionFile() != null && !profile.getPermissionFile().isEmpty()) {
                        try {
                            String oldPublicId = extractPublicIdFromUrl(profile.getPermissionFile());
                            if (oldPublicId != null) {
                                cloudinaryService.delete(oldPublicId);
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to delete old permission file: " + e.getMessage());
                        }
                    }
                    profile.setPermissionFile(fileUrl);
                    internService.save(profile);
                    response.put("internId", internId);
                    response.put("updated", true);
                } else {
                    response.put("warning", "Intern profile not found for internId: " + internId);
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/permission/{internId}")
    public ResponseEntity<Map<String, Object>> getPermissionFile(@PathVariable("internId") int internId) {
        try {
            Optional<InternProfile> profileOpt = internService.findById(internId);
            if (profileOpt.isPresent()) {
                InternProfile profile = profileOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("internId", internId);
                response.put("permissionFileUrl", profile.getPermissionFile() != null ? profile.getPermissionFile() : null);
                response.put("hasPermissionFile", profile.getPermissionFile() != null && !profile.getPermissionFile().isEmpty());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Intern profile not found for internId: " + internId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // ============================================
    // HELPER METHOD
    // ============================================

    /**
     * Extract public ID from Cloudinary URL
     * Example: https://res.cloudinary.com/cloud_name/image/upload/v1234567890/folder/filename.jpg
     * Returns: folder/filename
     */
    private String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            // Cloudinary URLs typically have /upload/ or /upload/v{version}/ followed by the public ID
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex != -1) {
                String afterUpload = url.substring(uploadIndex + 8);
                // Remove version if present (v1234567890/)
                if (afterUpload.matches("^v\\d+/.*")) {
                    afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
                }
                // Remove file extension
                int lastDot = afterUpload.lastIndexOf('.');
                if (lastDot != -1) {
                    afterUpload = afterUpload.substring(0, lastDot);
                }
                return afterUpload;
            }
        } catch (Exception e) {
            System.err.println("Failed to extract public ID from URL: " + url);
        }
        return null;
    }
}