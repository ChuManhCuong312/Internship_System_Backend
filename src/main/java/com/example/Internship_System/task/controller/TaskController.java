package com.example.Internship_System.task.controller;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //CREATE - Add new task
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        try{
            Task savedTask = taskService.save(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //READ get all task
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        try{
            List<Task> tasks = taskService.findAll();
            if(tasks.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
