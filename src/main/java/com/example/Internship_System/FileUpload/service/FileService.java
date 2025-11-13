package com.example.Internship_System.FileUpload.service;

import com.example.Internship_System.FileUpload.repository.FileUploadRepository;
import com.example.Internship_System.FileUpload.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class FileService {
    @Autowired
    private FileUploadRepository fileUploadRepository;

    public FileEntity uploadFile(MultipartFile file) throws IOException{
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setData(file.getBytes());
        return fileUploadRepository.save(fileEntity);
    }
    public FileEntity getFile(Long id){
        return fileUploadRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found with id: "+ id));
    }
    public List<FileEntity> getAllFiles(){
        return fileUploadRepository.findAll();
    }
}