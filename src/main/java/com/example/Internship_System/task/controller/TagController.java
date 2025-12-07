package com.example.Internship_System.task.controller;

import com.example.Internship_System.task.dto.TagDTO;
import com.example.Internship_System.task.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class TagController {

    @Autowired
    private TagService tagService;

    // Get all tags
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        try {
            List<TagDTO> tags = tagService.findAll();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get tag by ID
    @GetMapping("/{tagId}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Integer tagId) {
        try {
            return tagService.findById(tagId)
                    .map(tag -> new ResponseEntity<>(tag, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get tags by program ID
    @GetMapping("/program/{programId}")
    public ResponseEntity<List<TagDTO>> getTagsByProgram(@PathVariable Integer programId) {
        try {
            List<TagDTO> tags = tagService.findByProgramId(programId);
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create new tag
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagDTO tagDTO) {
        try {
            TagDTO createdTag = tagService.createTag(tagDTO);
            return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Lỗi khi tạo tag"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update tag
    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable Integer tagId, @RequestBody TagDTO tagDTO) {
        try {
            TagDTO updatedTag = tagService.updateTag(tagId, tagDTO);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Lỗi khi cập nhật tag"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete tag
    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer tagId) {
        try {
            tagService.deleteTag(tagId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Lỗi khi xóa tag"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get tags by task ID
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TagDTO>> getTagsByTask(@PathVariable Integer taskId) {
        try {
            List<TagDTO> tags = tagService.findTagsByTaskId(taskId);
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add tag to task
    @PostMapping("/task/{taskId}/tag/{tagId}")
    public ResponseEntity<?> addTagToTask(@PathVariable Integer taskId, @PathVariable Integer tagId) {
        try {
            tagService.addTagToTask(taskId, tagId);
            return new ResponseEntity<>(Map.of("message", "Thêm tag thành công"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Lỗi khi thêm tag"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove tag from task
    @DeleteMapping("/task/{taskId}/tag/{tagId}")
    public ResponseEntity<?> removeTagFromTask(@PathVariable Integer taskId, @PathVariable Integer tagId) {
        try {
            tagService.removeTagFromTask(taskId, tagId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Lỗi khi xóa tag khỏi task"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
