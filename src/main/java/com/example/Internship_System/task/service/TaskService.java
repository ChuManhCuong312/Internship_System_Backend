package com.example.Internship_System.task.service;

import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.repository.MentorRepository;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.TaskRepository;
import com.example.Internship_System.repository.TaskTeamAssignmentRepository;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.task.dto.TaskDTO;
import com.example.Internship_System.task.dto.TaskStatisticsDTO;
import com.example.Internship_System.task.entity.Task;
import com.example.Internship_System.team.entity.TeamIntern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public void deleteById(int id) {
        repository.deleteById(id);
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

        // Fetch mentor name from User entity
        Optional<MentorUser> mentor = mentorRepository.findById(task.getMentorId());
        if (mentor.isPresent() && mentor.get().getUser() != null) {
            dto.setMentorName(mentor.get().getUser().getFullName());
        }

        // Fetch program name
        Optional<Program> program = programRepository.findById(task.getProgramId());
        program.ifPresent(p -> dto.setProgramName(p.getName()));

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
