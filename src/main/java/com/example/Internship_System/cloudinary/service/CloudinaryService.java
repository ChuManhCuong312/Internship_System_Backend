package com.example.Internship_System.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> upload(MultipartFile file, String folder) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", folder));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> delete(String publicId) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
@SuppressWarnings("unused")
    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
@SuppressWarnings("unused")
    public String getImageUrl(String publicId, int width, int height) {
        return cloudinary.url()
                .transformation(new com.cloudinary.Transformation()
                        .width(width)
                        .height(height)
                        .crop("fill"))
                .generate(publicId);
    }
}