package com.example.Internship_System.task.controller;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import com.example.Internship_System.repository.TaskProgressRepository;
import com.example.Internship_System.task.entity.TaskProgress;
import com.example.Internship_System.task.dto.ProgressRequest;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskProgressRepository taskProgressRepository;

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

    // GET tasks for a specific intern
    @GetMapping("/my")
    public ResponseEntity<List<Task>> getTasksForIntern(@RequestParam(required = false) Integer internId){
        try{
            if (internId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            List<Task> tasks = taskService.findByInternId(internId);
            if(tasks.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add progress entry for a task
    @PutMapping("/{taskId}/progress")
    public ResponseEntity<?> addProgress(@PathVariable int taskId, @RequestBody ProgressRequest req){
        try{
            Optional<Task> opt = taskService.findById(taskId);
            if(opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            Task task = opt.get();

            TaskProgress p = new TaskProgress();
            p.setTask(task);
            p.setPercentComplete(req.getPercentComplete());
            p.setNote(req.getNote());
            p.setUpdatedAt(LocalDateTime.now());
            taskProgressRepository.save(p);

            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get progress history for task
    @GetMapping("/{taskId}/history")
    public ResponseEntity<List<TaskProgress>> getHistory(@PathVariable int taskId){
        try{
            List<TaskProgress> list = taskProgressRepository.findByTaskTaskIdOrderByUpdatedAtDesc(taskId);
            if(list.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update task status
    @PutMapping("/{taskId}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable int taskId, @RequestBody java.util.Map<String, String> body){
        try{
            Optional<Task> opt = taskService.findById(taskId);
            if(opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            Task task = opt.get();
            String status = body.get("status");
            if(status == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            task.setStatus(status);
            taskService.saveAndFlush(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
