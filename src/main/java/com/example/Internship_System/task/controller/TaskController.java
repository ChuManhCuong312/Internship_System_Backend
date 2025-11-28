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

    //READ get task by id
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id){
        try{
            Optional<Task> task = taskService.findById(id);
            return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ get tasks by mentor id
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<Task>> getTasksByMentorId(@PathVariable int mentorId){
        try{
            List<Task> tasks = taskService.findByMentorId(mentorId);
            if(tasks.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ get tasks by program id
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Task>> getTasksByProgramId(@PathVariable int programId){
        try{
            List<Task> tasks = taskService.findByProgramId(programId);
            if(tasks.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE - Update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task task){
        try{
            Optional<Task> existingTask = taskService.findById(id);
            if(existingTask.isPresent()){
                task.setTaskId(id);
                Task updatedTask = taskService.save(task);
                return new ResponseEntity<>(updatedTask, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE - Update task status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable int id, @RequestBody String status){
        try{
            Optional<Task> existingTask = taskService.findById(id);
            if(existingTask.isPresent()){
                Task task = existingTask.get();
                task.setStatus(status);
                Task updatedTask = taskService.save(task);
                return new ResponseEntity<>(updatedTask, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE - Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable int id){
        try{
            taskService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
