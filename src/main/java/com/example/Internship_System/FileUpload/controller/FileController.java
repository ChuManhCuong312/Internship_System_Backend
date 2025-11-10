package com.example.Internship_System.FileUpload.controller;

import com.example.Internship_System.FileUpload.service.FileService;
import com.example.Internship_System.FileUpload.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.saml2.LogoutResponseDsl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/files")
public class FileController{
    @Autowired
    private FileService fileService;
    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileEntity> uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
        FileEntity savedFile = fileService.uploadFile(file);
        return ResponseEntity.ok(savedFile);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id){
        FileEntity fileEntity = fileService.getFile(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileEntity.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"").body(new ByteArrayResource(fileEntity.getData()));
    }
    @GetMapping
    public ResponseEntity<List<FileEntity>> getAllFiles(){
        return ResponseEntity.ok(fileService.getAllFiles());
    }
}