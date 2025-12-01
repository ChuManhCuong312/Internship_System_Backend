package com.example.Internship_System.task.controller;

import com.example.Internship_System.task.dto.TaskFilesDTO;
import com.example.Internship_System.task.dto.TaskProgressDTO;
import com.example.Internship_System.task.entity.TaskFiles;
import com.example.Internship_System.task.entity.TaskProgress;
import com.example.Internship_System.task.entity.TaskTeamAssignment;
import com.example.Internship_System.task.service.TaskFilesService;
import com.example.Internship_System.task.service.TaskProgressService;
import com.example.Internship_System.repository.TaskTeamAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task-management")
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class TaskManagementController {

    @Autowired
    private TaskProgressService taskProgressService;

    @Autowired
    private TaskFilesService taskFilesService;

    @Autowired
    private TaskTeamAssignmentRepository taskTeamAssignmentRepository;

    // ======================================
    // TASK PROGRESS ENDPOINTS
    // ======================================

    /**
     * Create new task progress record
     */
    @PostMapping("/progress")
    public ResponseEntity<TaskProgress> createTaskProgress(@RequestBody TaskProgress taskProgress) {
        try {
            TaskProgress savedProgress = taskProgressService.save(taskProgress);
            return new ResponseEntity<>(savedProgress, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all task progress records
     */
    @GetMapping("/progress")
    public ResponseEntity<List<TaskProgressDTO>> getAllTaskProgress() {
        try {
            List<TaskProgressDTO> progressList = taskProgressService.findAllWithDetails();
            return new ResponseEntity<>(progressList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get task progress by progress ID
     */
    @GetMapping("/progress/{progressId}")
    public ResponseEntity<TaskProgress> getTaskProgressById(@PathVariable int progressId) {
        try {
            Optional<TaskProgress> progress = taskProgressService.findById(progressId);
            return progress.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get task progress by task ID
     */
    @GetMapping("/progress/task/{taskId}")
    public ResponseEntity<TaskProgressDTO> getTaskProgressByTaskId(@PathVariable int taskId) {
        try {
            TaskProgressDTO progress = taskProgressService.findByTaskIdWithDetails(taskId);
            if (progress != null) {
                return new ResponseEntity<>(progress, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update task progress
     */
    @PutMapping("/progress/{progressId}")
    public ResponseEntity<TaskProgress> updateTaskProgress(@PathVariable int progressId, @RequestBody TaskProgress taskProgress) {
        try {
            Optional<TaskProgress> existingProgress = taskProgressService.findById(progressId);
            if (existingProgress.isPresent()) {
                taskProgress.setProgressId(progressId);
                TaskProgress updatedProgress = taskProgressService.save(taskProgress);
                return new ResponseEntity<>(updatedProgress, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update task progress percentage
     */
    @PatchMapping("/progress/{progressId}/percentage")
    public ResponseEntity<TaskProgress> updateProgressPercentage(@PathVariable int progressId, @RequestParam int percentage) {
        try {
            Optional<TaskProgress> existingProgress = taskProgressService.findById(progressId);
            if (existingProgress.isPresent()) {
                TaskProgress progress = existingProgress.get();
                if (percentage < 0 || percentage > 100) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                progress.setPercentComplete(percentage);
                TaskProgress updatedProgress = taskProgressService.save(progress);
                return new ResponseEntity<>(updatedProgress, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete task progress
     */
    @DeleteMapping("/progress/{progressId}")
    public ResponseEntity<HttpStatus> deleteTaskProgress(@PathVariable int progressId) {
        try {
            taskProgressService.deleteById(progressId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================================
    // TASK FILES ENDPOINTS
    // ======================================

    /**
     * Create new task file record
     */
    @PostMapping("/files")
    public ResponseEntity<TaskFiles> createTaskFile(@RequestBody TaskFiles taskFiles) {
        try {
            TaskFiles savedFile = taskFilesService.save(taskFiles);
            return new ResponseEntity<>(savedFile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all task files
     */
    @GetMapping("/files")
    public ResponseEntity<List<TaskFilesDTO>> getAllTaskFiles() {
        try {
            List<TaskFilesDTO> filesList = taskFilesService.findAllWithDetails();
            return new ResponseEntity<>(filesList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get task file by file ID
     */
    @GetMapping("/files/{fileId}")
    public ResponseEntity<TaskFiles> getTaskFileById(@PathVariable int fileId) {
        try {
            Optional<TaskFiles> file = taskFilesService.findById(fileId);
            return file.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all files for a specific task
     */
    @GetMapping("/files/task/{taskId}")
    public ResponseEntity<List<TaskFilesDTO>> getFilesByTaskId(@PathVariable int taskId) {
        try {
            List<TaskFilesDTO> files = taskFilesService.findByTaskIdWithDetails(taskId);
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update task file
     */
    @PutMapping("/files/{fileId}")
    public ResponseEntity<TaskFiles> updateTaskFile(@PathVariable int fileId, @RequestBody TaskFiles taskFiles) {
        try {
            Optional<TaskFiles> existingFile = taskFilesService.findById(fileId);
            if (existingFile.isPresent()) {
                taskFiles.setTaskFilesId(fileId);
                TaskFiles updatedFile = taskFilesService.save(taskFiles);
                return new ResponseEntity<>(updatedFile, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete task file
     */
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<HttpStatus> deleteTaskFile(@PathVariable int fileId) {
        try {
            taskFilesService.deleteById(fileId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================================
    // TASK TEAM ASSIGNMENT ENDPOINTS
    // ======================================

    /**
     * Create new task team assignment
     */
    @PostMapping("/team-assignments")
    public ResponseEntity<TaskTeamAssignment> createTaskTeamAssignment(@RequestBody TaskTeamAssignment assignment) {
        try {
            TaskTeamAssignment savedAssignment = taskTeamAssignmentRepository.save(assignment);
            return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all task team assignments
     */
    @GetMapping("/team-assignments")
    public ResponseEntity<List<TaskTeamAssignment>> getAllTaskTeamAssignments() {
        try {
            List<TaskTeamAssignment> assignments = taskTeamAssignmentRepository.findAll();
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get task team assignment by ID
     */
    @GetMapping("/team-assignments/{assignmentId}")
    public ResponseEntity<TaskTeamAssignment> getTaskTeamAssignmentById(@PathVariable int assignmentId) {
        try {
            Optional<TaskTeamAssignment> assignment = taskTeamAssignmentRepository.findById(assignmentId);
            return assignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get assignments by task ID
     */
    @GetMapping("/team-assignments/task/{taskId}")
    public ResponseEntity<List<TaskTeamAssignment>> getAssignmentsByTaskId(@PathVariable int taskId) {
        try {
            List<TaskTeamAssignment> assignments = taskTeamAssignmentRepository.findByTaskId(taskId);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get assignments by team ID
     */
    @GetMapping("/team-assignments/team/{teamId}")
    public ResponseEntity<List<TaskTeamAssignment>> getAssignmentsByTeamId(@PathVariable int teamId) {
        try {
            List<TaskTeamAssignment> assignments = taskTeamAssignmentRepository.findByTeamId(teamId);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update task team assignment
     */
    @PutMapping("/team-assignments/{assignmentId}")
    public ResponseEntity<TaskTeamAssignment> updateTaskTeamAssignment(@PathVariable int assignmentId, @RequestBody TaskTeamAssignment assignment) {
        try {
            Optional<TaskTeamAssignment> existingAssignment = taskTeamAssignmentRepository.findById(assignmentId);
            if (existingAssignment.isPresent()) {
                assignment.setId(assignmentId);
                TaskTeamAssignment updatedAssignment = taskTeamAssignmentRepository.save(assignment);
                return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete task team assignment
     */
    @DeleteMapping("/team-assignments/{assignmentId}")
    public ResponseEntity<HttpStatus> deleteTaskTeamAssignment(@PathVariable int assignmentId) {
        try {
            taskTeamAssignmentRepository.deleteById(assignmentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
