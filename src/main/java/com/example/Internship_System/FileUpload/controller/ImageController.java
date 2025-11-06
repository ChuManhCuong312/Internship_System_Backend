package com.example.Internship_System.FileUpload.controller;
import com.example.Internship_System.FileUpload.entity.Image;
import com.example.Internship_System.FileUpload.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

    @RestController
    @RequestMapping("/api/images")
    @CrossOrigin(origins = "*")
    public class ImageController {

        @Autowired
        private ImageService imageService;

        // Upload image
        @PostMapping("/upload")
        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
            try {
                Image image = imageService.saveImage(file);
                return ResponseEntity.ok("Image uploaded successfully with ID: " + image.getId());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error uploading image: " + e.getMessage());
            }
        }

        // Get image by ID
        @GetMapping("/{id}")
        public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
            Optional<Image> imageOptional = imageService.getImage(id);

            if (imageOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Image image = imageOptional.get();

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(image.getType()))
                    .body(image.getData());
        }
    }
}
