package com.example.Internship_System.task.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagDTO {
    private Integer tagId;
    private String name;
    private String color;
    private Integer programId;

    public TagDTO() {}

    public TagDTO(Integer tagId, String name, String color, Integer programId) {
        this.tagId = tagId;
        this.name = name;
        this.color = color;
        this.programId = programId;
    }
}
