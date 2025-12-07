package com.example.Internship_System.task.service;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.repository.MentorRepository;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.TagRepository;
import com.example.Internship_System.repository.TaskFilesRepository;
import com.example.Internship_System.repository.TaskProgressRepository;
import com.example.Internship_System.repository.TaskRepository;
import com.example.Internship_System.repository.TaskTagRepository;
import com.example.Internship_System.repository.TaskTeamAssignmentRepository;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.task.dto.TagDTO;
import com.example.Internship_System.task.dto.TaskDTO;
import com.example.Internship_System.task.dto.TaskStatisticsDTO;
import com.example.Internship_System.task.dto.TaskUpdateRequest;
import com.example.Internship_System.task.entity.Tag;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.task.entity.TaskFiles;
import com.example.Internship_System.task.entity.TaskProgress;
import com.example.Internship_System.task.entity.TaskTeamAssignment;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private TeamInternRepository teamInternRepository;

    @Autowired
    private TaskTeamAssignmentRepository taskTeamAssignmentRepository;

    @Autowired
    private TaskFilesRepository taskFilesRepository;

    @Autowired
    private TaskProgressRepository taskProgressRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TaskTagRepository taskTagRepository;

    public Task save(Task task) {
        return repository.save(task);
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }
@SuppressWarnings("unused")
    public List<Task> findByMentorId(int mentorId) {
        return repository.findByMentorId(mentorId);
    }
@SuppressWarnings("unused")
    public List<Task> findByProgramId(int programId) {
        return repository.findByProgramId(programId);
    }

    @Transactional
    public void deleteById(int id) {
        // Xóa các bản ghi liên quan trước khi xóa task
        taskTeamAssignmentRepository.deleteByTaskId(id);
        taskFilesRepository.deleteByTaskId(id);
        taskProgressRepository.deleteByTaskId(id);
        taskTagRepository.deleteByTaskId(id);
        repository.deleteById(id);
    }

    @Transactional
    public Task updateTask(int id, Task taskUpdate) {
        Task existingTask = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task không tồn tại với id: " + id));
        
        // Cập nhật các trường từ request, giữ nguyên các trường không được gửi
        if (taskUpdate.getTitle() != null) {
            existingTask.setTitle(taskUpdate.getTitle());
        }
        if (taskUpdate.getDescription() != null) {
            existingTask.setDescription(taskUpdate.getDescription());
        }
        if (taskUpdate.getStatus() != null) {
            existingTask.setStatus(taskUpdate.getStatus());
        }
        if (taskUpdate.getPriority() != null) {
            existingTask.setPriority(taskUpdate.getPriority());
        }
        if (taskUpdate.getDeadline() != null) {
            existingTask.setDeadline(taskUpdate.getDeadline());
        }
        if (taskUpdate.getProgramId() != 0) {
            existingTask.setProgramId(taskUpdate.getProgramId());
        }
        if (taskUpdate.getMentorId() != 0) {
            existingTask.setMentorId(taskUpdate.getMentorId());
        }
        if (taskUpdate.getAssignedBy() != null) {
            existingTask.setAssignedBy(taskUpdate.getAssignedBy());
        }
        existingTask.setDue_soon(taskUpdate.isDue_soon());
        
        return repository.save(existingTask);
    }

    /**
     * Tạo task mới với đầy đủ thông tin liên quan (teams, files, progress)
     * Xử lý tất cả trong 1 transaction
     */
    @Transactional
    public Task createTaskFull(TaskUpdateRequest request) {
        // 1. Tạo task
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : "TODO");
        task.setPriority(request.getPriority() != null ? request.getPriority() : "MEDIUM");
        task.setDeadline(request.getDeadline());
        task.setProgramId(request.getProgramId());
        task.setMentorId(request.getMentorId());
        task.setAssignedBy(request.getAssignedBy());
        task.setDue_soon(request.isDueSoon());
        task.setCreated_at(LocalDateTime.now());
        
        Task savedTask = repository.save(task);
        int taskId = savedTask.getTaskId();
        
        // 2. Gán teams
        if (request.getTeamIds() != null && !request.getTeamIds().isEmpty()) {
            for (Integer teamId : request.getTeamIds()) {
                TaskTeamAssignment assignment = new TaskTeamAssignment();
                assignment.setTaskId(taskId);
                assignment.setTeamId(teamId);
                taskTeamAssignmentRepository.save(assignment);
            }
        }
        
        // 3. Thêm files
        if (request.getFileLinks() != null && !request.getFileLinks().isEmpty()) {
            for (String link : request.getFileLinks()) {
                TaskFiles file = new TaskFiles(taskId, link);
                taskFilesRepository.save(file);
            }
        }
        
        // 4. Tạo progress ban đầu
        if (request.getProgressPercent() != null) {
            TaskProgress progress = new TaskProgress(taskId, request.getProgressPercent(), request.getProgressNote());
            taskProgressRepository.save(progress);
        }

        // 5. Gán tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Integer tagId : request.getTagIds()) {
                if (tagRepository.existsById(tagId)) {
                    taskTagRepository.save(new com.example.Internship_System.task.entity.TaskTag(taskId, tagId));
                }
            }
        }
        
        return savedTask;
    }

    /**
     * Cập nhật task với đầy đủ thông tin liên quan (teams, files, progress)
     * Xử lý tất cả trong 1 transaction
     */
    @Transactional
    public Task updateTaskFull(int id, TaskUpdateRequest request) {
        Task existingTask = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task không tồn tại với id: " + id));
        
        // 1. Cập nhật task info
        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            existingTask.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            existingTask.setPriority(request.getPriority());
        }
        if (request.getDeadline() != null) {
            existingTask.setDeadline(request.getDeadline());
        }
        if (request.getProgramId() != 0) {
            existingTask.setProgramId(request.getProgramId());
        }
        if (request.getMentorId() != 0) {
            existingTask.setMentorId(request.getMentorId());
        }
        if (request.getAssignedBy() != null) {
            existingTask.setAssignedBy(request.getAssignedBy());
        }
        existingTask.setDue_soon(request.isDueSoon());
        
        Task savedTask = repository.save(existingTask);
        
        // 2. Cập nhật team assignments (nếu có gửi teamIds)
        if (request.getTeamIds() != null) {
            taskTeamAssignmentRepository.deleteByTaskId(id);
            for (Integer teamId : request.getTeamIds()) {
                TaskTeamAssignment assignment = new TaskTeamAssignment();
                assignment.setTaskId(id);
                assignment.setTeamId(teamId);
                taskTeamAssignmentRepository.save(assignment);
            }
        }
        
        // 3. Cập nhật files (nếu có gửi fileLinks)
        if (request.getFileLinks() != null) {
            taskFilesRepository.deleteByTaskId(id);
            for (String link : request.getFileLinks()) {
                TaskFiles file = new TaskFiles(id, link);
                taskFilesRepository.save(file);
            }
        }
        
        // 4. Cập nhật progress (nếu có gửi progressPercent)
        if (request.getProgressPercent() != null) {
            taskProgressRepository.deleteByTaskId(id);
            TaskProgress progress = new TaskProgress(id, request.getProgressPercent(), request.getProgressNote());
            taskProgressRepository.save(progress);
        }

        // 5. Cập nhật tags (nếu có gửi tagIds)
        if (request.getTagIds() != null) {
            taskTagRepository.deleteByTaskId(id);
            for (Integer tagId : request.getTagIds()) {
                if (tagRepository.existsById(tagId)) {
                    taskTagRepository.save(new com.example.Internship_System.task.entity.TaskTag(id, tagId));
                }
            }
        }
        
        return savedTask;
    }
@SuppressWarnings("unused")
    public List<Task> findAllSorted(String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository.findAll(Sort.by(sortDirection, sortBy));
    }
@SuppressWarnings("unused")
    public List<Task> filterTasks(Integer mentorId, Integer programId, String status, 
                                 String priority, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.filterTasks(mentorId, programId, status, priority, startDate, endDate);
    }
@SuppressWarnings("unused")
    private int compareByField(Task a, Task b, String field) {
        return switch (field.toLowerCase()) {
            case "taskid" -> Integer.compare(a.getTaskId(), b.getTaskId());
            case "mentorid" -> Integer.compare(a.getMentorId(), b.getMentorId());
            case "programid" -> Integer.compare(a.getProgramId(), b.getProgramId());
            case "title" -> a.getTitle().compareTo(b.getTitle());
            case "status" -> a.getStatus() != null ? a.getStatus().compareTo(b.getStatus() != null ? b.getStatus() : "") : 0;
            case "priority" -> a.getPriority() != null ? a.getPriority().compareTo(b.getPriority() != null ? b.getPriority() : "") : 0;
            case "deadline" -> a.getDeadline().compareTo(b.getDeadline());
            case "createdat" -> a.getCreated_at() != null ? a.getCreated_at().compareTo(b.getCreated_at()) : 0;
            default -> 0;
        };
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setProgramId(task.getProgramId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setAssignedBy(task.getAssignedBy() != null ? task.getAssignedBy().toString() : null);
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreated_at());
        dto.setDeadline(task.getDeadline());
        dto.setDueSoon(task.isDue_soon());
        dto.setPriority(task.getPriority());
        dto.setMentorId(task.getMentorId());

        try {
            // Fetch mentor name from User entity
            if (task.getMentorId() > 0) {
                Optional<MentorUser> mentor = mentorRepository.findById(task.getMentorId());
                if (mentor.isPresent() && mentor.get().getUser() != null) {
                    dto.setMentorName(mentor.get().getUser().getFullName());
                }
            }

            // Fetch program name
            if (task.getProgramId() > 0) {
                Optional<Program> program = programRepository.findById(task.getProgramId());
                program.ifPresent(p -> dto.setProgramName(p.getName()));
            }

            // Fetch tags for this task
            List<Integer> tagIds = taskTagRepository.findTagIdsByTaskId(task.getTaskId());
            if (!tagIds.isEmpty()) {
                List<Tag> tags = tagRepository.findAllById(tagIds);
                List<TagDTO> tagDTOs = tags.stream()
                        .map(tag -> new TagDTO(tag.getTagId(), tag.getName(), tag.getColor(), tag.getProgramId()))
                        .toList();
                dto.setTags(tagDTOs);
            } else {
                dto.setTags(new ArrayList<>());
            }
        } catch (Exception e) {
            // Log error but don't fail - return DTO with partial data
            System.err.println("Warning: Error fetching mentor/program/tags details for task " + task.getTaskId() + ": " + e.getMessage());
            dto.setTags(new ArrayList<>());
        }

        return dto;
    }

    public List<TaskDTO> findAllWithDetails() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TaskDTO> findByMentorIdWithDetails(int mentorId) {
        return repository.findByMentorId(mentorId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TaskDTO> findByProgramIdWithDetails(int programId) {
        return repository.findByProgramId(programId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TaskDTO> filterTasksWithDetails(Integer mentorId, Integer programId, String status,
                                                String priority, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.filterTasks(mentorId, programId, status, priority, startDate, endDate).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TaskDTO> findTasksByIntern(Integer internId) {
        // 1) Get team of intern
        TeamIntern teamIntern = teamInternRepository.findByInternId(internId);
        if (teamIntern == null || teamIntern.getTeam() == null) {
            return new ArrayList<>();
        }

        Integer teamId = teamIntern.getTeam().getTeamId();

        // 2) Get task ids assigned to team
        List<Integer> taskIds = taskTeamAssignmentRepository.findTaskIdsByTeamId(teamId);
        if (taskIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 3) Get all tasks by id
        List<Task> tasks = repository.findByTaskIdIn(taskIds);

        // 4) Convert to DTO
        return tasks.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TaskStatisticsDTO getTaskStatisticsForIntern(Integer internId) {
        List<TaskDTO> tasks = findTasksByIntern(internId);
        
        int inProgress = (int) tasks.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count();
        int todo = (int) tasks.stream().filter(t -> "TODO".equals(t.getStatus())).count();
        int done = (int) tasks.stream().filter(t -> "DONE".equals(t.getStatus())).count();
        int total = tasks.size();
        
        return new TaskStatisticsDTO(inProgress, todo, done, total);
    }
}
