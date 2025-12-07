package com.example.Internship_System.task.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @NotBlank(message = "Tên tag là bắt buộc")
    @Size(max = 50, message = "Tên tag không được quá 50 ký tự")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "program_id", nullable = false)
    private Integer programId;

    public Tag() {}

    public Tag(String name, String color, Integer programId) {
        this.name = name;
        this.color = color;
        this.programId = programId;
    }
}
