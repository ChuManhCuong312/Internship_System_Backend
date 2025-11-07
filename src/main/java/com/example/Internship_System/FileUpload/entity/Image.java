package com.example.Internship_System.FileUpload.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    @Column(name = "image_name")
    private String imageName;
    @Column(name = "type")
    private String type;
    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;
    public Image(){}

    public Image(String imageName, String type, byte[] data) {
        this.imageName = imageName;
        this.type = type;
        this.data = data;
    }

    // Getters and setters
    public Long getImageId(){ return imageId; }
    public String getImageName() { return imageName; }
    public void setImageName(String name) { this.imageName = imageName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}