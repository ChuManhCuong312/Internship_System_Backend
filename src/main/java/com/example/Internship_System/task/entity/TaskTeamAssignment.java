package com.example.Internship_System.task.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "task_team_assignments")
public class TaskTeamAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "team_id", nullable = false)
    private Integer teamId;
}
