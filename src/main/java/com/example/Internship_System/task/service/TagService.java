package com.example.Internship_System.task.service;

import com.example.Internship_System.repository.TagRepository;
import com.example.Internship_System.repository.TaskTagRepository;
import com.example.Internship_System.task.dto.TagDTO;
import com.example.Internship_System.task.entity.Tag;
import com.example.Internship_System.task.entity.TaskTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TaskTagRepository taskTagRepository;

    // Convert entity to DTO
    private TagDTO convertToDTO(Tag tag) {
        return new TagDTO(tag.getTagId(), tag.getName(), tag.getColor(), tag.getProgramId());
    }

    // Get all tags
    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get tag by ID
    public Optional<TagDTO> findById(Integer tagId) {
        return tagRepository.findById(tagId).map(this::convertToDTO);
    }

    // Get tags by program ID
    public List<TagDTO> findByProgramId(Integer programId) {
        return tagRepository.findByProgramId(programId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create new tag
    @Transactional
    public TagDTO createTag(TagDTO tagDTO) {
        // Check if tag with same name already exists in program
        if (tagRepository.existsByNameAndProgramId(tagDTO.getName(), tagDTO.getProgramId())) {
            throw new RuntimeException("Tag với tên này đã tồn tại trong chương trình");
        }

        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setColor(tagDTO.getColor() != null ? tagDTO.getColor() : "#3b82f6");
        tag.setProgramId(tagDTO.getProgramId());

        Tag savedTag = tagRepository.save(tag);
        return convertToDTO(savedTag);
    }

    // Update tag
    @Transactional
    public TagDTO updateTag(Integer tagId, TagDTO tagDTO) {
        Tag existingTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag không tồn tại với id: " + tagId));

        // Check if new name conflicts with existing tag in same program
        if (tagDTO.getName() != null && !tagDTO.getName().equals(existingTag.getName())) {
            if (tagRepository.existsByNameAndProgramId(tagDTO.getName(), existingTag.getProgramId())) {
                throw new RuntimeException("Tag với tên này đã tồn tại trong chương trình");
            }
            existingTag.setName(tagDTO.getName());
        }

        if (tagDTO.getColor() != null) {
            existingTag.setColor(tagDTO.getColor());
        }

        Tag savedTag = tagRepository.save(existingTag);
        return convertToDTO(savedTag);
    }

    // Delete tag
    @Transactional
    public void deleteTag(Integer tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new RuntimeException("Tag không tồn tại với id: " + tagId);
        }
        // Delete all task-tag associations first
        taskTagRepository.deleteByTagId(tagId);
        // Then delete the tag
        tagRepository.deleteById(tagId);
    }

    // Add tag to task
    @Transactional
    public void addTagToTask(Integer taskId, Integer tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new RuntimeException("Tag không tồn tại với id: " + tagId);
        }
        if (taskTagRepository.existsByTaskIdAndTagId(taskId, tagId)) {
            throw new RuntimeException("Task đã có tag này");
        }

        TaskTag taskTag = new TaskTag(taskId, tagId);
        taskTagRepository.save(taskTag);
    }

    // Remove tag from task
    @Transactional
    public void removeTagFromTask(Integer taskId, Integer tagId) {
        TaskTag taskTag = taskTagRepository.findByTaskIdAndTagId(taskId, tagId)
                .orElseThrow(() -> new RuntimeException("Task không có tag này"));
        taskTagRepository.delete(taskTag);
    }

    // Get tags by task ID
    public List<TagDTO> findTagsByTaskId(Integer taskId) {
        List<Integer> tagIds = taskTagRepository.findTagIdsByTaskId(taskId);
        return tagRepository.findAllById(tagIds).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update tags for a task (replace all)
    @Transactional
    public void updateTaskTags(Integer taskId, List<Integer> tagIds) {
        // Remove all existing tags
        taskTagRepository.deleteByTaskId(taskId);
        
        // Add new tags
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                if (tagRepository.existsById(tagId)) {
                    TaskTag taskTag = new TaskTag(taskId, tagId);
                    taskTagRepository.save(taskTag);
                }
            }
        }
    }
}
