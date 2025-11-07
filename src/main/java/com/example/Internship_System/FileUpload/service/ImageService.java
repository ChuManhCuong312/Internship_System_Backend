package com.example.Internship_System.FileUpload.service;

import com.example.Internship_System.FileUpload.repository.ImageRepository;
import com.example.Internship_System.FileUpload.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    public Image saveImage(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        Image image = new Image(fileName, file.getContentType(), file.getBytes());
        return imageRepository.save(image);
    }
    public Optional<Image> getImage(Long imageId)
    {
        return imageRepository.findById(imageId);
    }
}