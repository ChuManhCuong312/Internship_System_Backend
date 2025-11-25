package com.example.Internship_System.task.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_team_assignments")
public class TaskTeamAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer taskId;
    private Integer teamId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }
}
