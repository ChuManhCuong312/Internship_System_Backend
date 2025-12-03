package com.example.Internship_System.task.controller;
import com.example.Internship_System.task.dto.PaginatedTaskDTO;
import com.example.Internship_System.task.dto.TaskDTO;
import com.example.Internship_System.task.dto.TaskStatisticsDTO;
import com.example.Internship_System.task.dto.TaskUpdateRequest;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //CREATE - Add new task with full related data (teams, files, progress)
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskUpdateRequest request){
        try{
            Task savedTask = taskService.createTaskFull(request);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //READ all tasks with details and pagination info
    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<TaskDTO> tasks = taskService.findAllWithDetails();
            
            // Apply sorting if sortBy is specified
            if (sortBy != null && !sortBy.isEmpty()) {
                tasks = tasks.stream()
                        .sorted((a, b) -> {
                            int comparison;
                            switch (sortBy.toLowerCase()) {
                                case "taskid" -> comparison = Integer.compare(a.getTaskId(), b.getTaskId());
                                case "mentorid" -> comparison = Integer.compare(a.getMentorId(), b.getMentorId());
                                case "programid" -> comparison = Integer.compare(a.getProgramId(), b.getProgramId());
                                case "title" -> comparison = a.getTitle().compareTo(b.getTitle());
                                case "status" -> comparison = a.getStatus() != null ? a.getStatus().compareTo(b.getStatus()) : 0;
                                case "priority" -> comparison = a.getPriority() != null ? a.getPriority().compareTo(b.getPriority()) : 0;
                                case "deadline" -> comparison = a.getDeadline().compareTo(b.getDeadline());
                                default -> comparison = 0;
                            }
                            return "desc".equalsIgnoreCase(direction) ? -comparison : comparison;
                        })
                        .toList();
            }
            
            if (page != null && size != null) {
                int start = page * size;
                int end = Math.min(start + size, tasks.size());
                List<TaskDTO> paginatedTasks = tasks.subList(start, end);
                PaginatedTaskDTO response = new PaginatedTaskDTO(paginatedTasks, tasks.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
        } catch (Exception e) {
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

    //READ tasks by mentor id with details and pagination info
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<?> getTasksByMentorId(
            @PathVariable("mentorId") int mentorId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<TaskDTO> tasks = taskService.findByMentorIdWithDetails(mentorId);
            
            if (page != null && size != null) {
                int start = page * size;
                int end = Math.min(start + size, tasks.size());
                List<TaskDTO> paginatedTasks = tasks.subList(start, end);
                PaginatedTaskDTO response = new PaginatedTaskDTO(paginatedTasks, tasks.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ tasks by program id with details and pagination info
    @GetMapping("/program/{programId}")
    public ResponseEntity<?> getTasksByProgramId(
            @PathVariable("programId") int programId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<TaskDTO> tasks = taskService.findByProgramIdWithDetails(programId);
            
            if (page != null && size != null) {
                int start = page * size;
                int end = Math.min(start + size, tasks.size());
                List<TaskDTO> paginatedTasks = tasks.subList(start, end);
                PaginatedTaskDTO response = new PaginatedTaskDTO(paginatedTasks, tasks.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error fetching tasks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ tasks by intern id with details
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getTasksByInternId(
            @PathVariable("internId") int internId) {
        try {
            List<TaskDTO> tasks = taskService.findTasksByIntern(internId);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/intern/{internId}/statistics")
    public ResponseEntity<?> getTaskStatisticsForIntern(
            @PathVariable("internId") int internId) {
        try {
            TaskStatisticsDTO stats = taskService.getTaskStatisticsForIntern(internId);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE - Update task with full related data (teams, files, progress) in one request
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody TaskUpdateRequest request){
        try{
            Task updatedTask = taskService.updateTaskFull(id, request);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //FILTER tasks with details and pagination info
    @GetMapping("/filter/search")
    public ResponseEntity<?> filterTasks(
            @RequestParam(required = false) Integer mentorId,
            @RequestParam(required = false) Integer programId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            LocalDateTime start = startDate != null && !startDate.isEmpty() ? LocalDateTime.parse(startDate) : null;
            LocalDateTime end = endDate != null && !endDate.isEmpty() ? LocalDateTime.parse(endDate) : null;
            
            List<TaskDTO> allTasks = taskService.filterTasksWithDetails(mentorId, programId, status, priority, start, end);
            
            if (page != null && size != null) {
                int start_idx = page * size;
                int end_idx = Math.min(start_idx + size, allTasks.size());
                List<TaskDTO> paginatedTasks = allTasks.subList(start_idx, end_idx);
                PaginatedTaskDTO response = new PaginatedTaskDTO(paginatedTasks, allTasks.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allTasks, HttpStatus.OK);
            }
        } catch (Exception e) {
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
