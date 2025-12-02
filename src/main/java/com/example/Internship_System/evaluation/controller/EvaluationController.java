
package com.example.Internship_System.evaluation.controller;

import com.example.Internship_System.evaluation.service.EvaluationService;
import com.example.Internship_System.evaluation.service.EvaluationService.TeamInfoDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Internship_System.evaluation.DTO.EvaluationRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamInfoDTO>> getTeamEvaluations(@PathVariable Integer teamId) {
        TeamInfoDTO dto = evaluationService.buildTeamInfo(teamId);
        return ResponseEntity.ok(Collections.singletonList(dto));
    }
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid EvaluationRequest req) {
        evaluationService.createEvaluation(req);
        return ResponseEntity.ok("Tạo đánh giá thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @RequestBody @Valid EvaluationRequest req
    ) {
        evaluationService.updateEvaluation(id, req);
        return ResponseEntity.ok("Chỉnh sửa đánh giá thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.ok("Xóa đánh giá thành công");
    }
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationService.EvaluationDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(evaluationService.getById(id));
    }



}