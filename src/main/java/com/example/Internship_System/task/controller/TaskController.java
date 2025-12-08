package com.example.Internship_System.task.controller;
import com.example.Internship_System.task.dto.PaginatedTaskDTO;
import com.example.Internship_System.task.dto.TaskDTO;
import com.example.Internship_System.task.dto.TaskStatisticsDTO;
import com.example.Internship_System.task.dto.TaskUpdateRequest;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class TaskController {
    private final TaskService taskService;

    //CREATE - Add new task with full related data (teams, files, progress)
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskUpdateRequest request){
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
            
            if (page != null && size != null && size > 0) {
                int start = page * size;
                if (start >= tasks.size()) {
                    PaginatedTaskDTO response = new PaginatedTaskDTO(Collections.emptyList(), tasks.size(), page, size);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
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
            
            // Apply sorting if sortBy is specified
            if (sortBy != null && !sortBy.isEmpty()) {
                tasks = applySorting(tasks, sortBy, direction);
            }
            
            if (page != null && size != null && size > 0) {
                int start = page * size;
                if (start >= tasks.size()) {
                    PaginatedTaskDTO response = new PaginatedTaskDTO(Collections.emptyList(), tasks.size(), page, size);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
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
            
            // Apply sorting if sortBy is specified
            if (sortBy != null && !sortBy.isEmpty()) {
                tasks = applySorting(tasks, sortBy, direction);
            }
            
            if (page != null && size != null && size > 0) {
                int start = page * size;
                if (start >= tasks.size()) {
                    PaginatedTaskDTO response = new PaginatedTaskDTO(Collections.emptyList(), tasks.size(), page, size);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
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
    public ResponseEntity<Task> updateTask(@PathVariable int id, @Valid @RequestBody TaskUpdateRequest request){
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
            @RequestParam(required = false) String tagIds,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            LocalDateTime start = startDate != null && !startDate.isEmpty() ? LocalDateTime.parse(startDate) : null;
            LocalDateTime end = endDate != null && !endDate.isEmpty() ? LocalDateTime.parse(endDate) : null;
            
            List<TaskDTO> allTasks = taskService.filterTasksWithDetails(mentorId, programId, status, priority, start, end);
            
            // Filter by multiple tagIds if provided
            if (tagIds != null && !tagIds.trim().isEmpty()) {
                String[] tagIdArray = tagIds.split(",");
                java.util.Set<Integer> tagIdSet = new java.util.HashSet<>();
                for (String id : tagIdArray) {
                    try {
                        tagIdSet.add(Integer.parseInt(id.trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid tag IDs
                    }
                }
                
                if (!tagIdSet.isEmpty()) {
                    allTasks = allTasks.stream()
                            .filter(task -> task.getTags() != null && 
                                    task.getTags().stream().anyMatch(tag -> tagIdSet.contains(tag.getTagId())))
                            .toList();
                }
            }

            // Filter by searchText if provided
            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchLower = searchText.toLowerCase().trim();
                allTasks = allTasks.stream()
                        .filter(task -> 
                                (task.getTitle() != null && task.getTitle().toLowerCase().contains(searchLower)) ||
                                (task.getDescription() != null && task.getDescription().toLowerCase().contains(searchLower)))
                        .toList();
            }
            
            if (page != null && size != null && size > 0) {
                int start_idx = page * size;
                if (start_idx >= allTasks.size()) {
                    PaginatedTaskDTO response = new PaginatedTaskDTO(Collections.emptyList(), allTasks.size(), page, size);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
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

    /**
     * Helper method to apply sorting to task list
     */
    private List<TaskDTO> applySorting(List<TaskDTO> tasks, String sortBy, String direction) {
        return tasks.stream()
                .sorted((a, b) -> {
                    int comparison;
                    switch (sortBy.toLowerCase()) {
                        case "taskid" -> comparison = Integer.compare(a.getTaskId(), b.getTaskId());
                        case "mentorid" -> comparison = Integer.compare(a.getMentorId(), b.getMentorId());
                        case "programid" -> comparison = Integer.compare(a.getProgramId(), b.getProgramId());
                        case "title" -> comparison = compareNullSafe(a.getTitle(), b.getTitle());
                        case "status" -> comparison = compareNullSafe(a.getStatus(), b.getStatus());
                        case "priority" -> comparison = compareNullSafe(a.getPriority(), b.getPriority());
                        case "deadline" -> comparison = compareNullSafe(a.getDeadline(), b.getDeadline());
                        default -> comparison = 0;
                    }
                    return "desc".equalsIgnoreCase(direction) ? -comparison : comparison;
                })
                .toList();
    }

    private <T extends Comparable<T>> int compareNullSafe(T a, T b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
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
