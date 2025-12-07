package com.example.Internship_System.task.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "task_tags")
public class TaskTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    public TaskTag() {}

    public TaskTag(Integer taskId, Integer tagId) {
        this.taskId = taskId;
        this.tagId = tagId;
    }
}
