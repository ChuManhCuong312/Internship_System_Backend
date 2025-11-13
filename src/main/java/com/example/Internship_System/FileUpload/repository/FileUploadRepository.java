package com.example.Internship_System.FileUpload.repository;

import com.example.Internship_System.FileUpload.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FileUploadRepository extends JpaRepository<FileEntity, Long> {
}