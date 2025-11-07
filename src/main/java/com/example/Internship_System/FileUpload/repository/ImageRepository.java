package com.example.Internship_System.FileUpload.repository;

import com.example.Internship_System.FileUpload.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ImageRepository extends JpaRepository<Image, Long> {
}