package com.example.Internship_System.cloudinary.controller;

import com.example.Internship_System.cloudinary.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@CrossOrigin(origins = "*")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

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
}