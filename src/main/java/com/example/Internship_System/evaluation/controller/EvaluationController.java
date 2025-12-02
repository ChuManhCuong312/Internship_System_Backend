
package com.example.Internship_System.evaluation.controller;

import com.example.Internship_System.evaluation.service.EvaluationService;
import com.example.Internship_System.evaluation.service.EvaluationService.TeamInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * Trả về mảng TEAM_INFO có 1 phần tử chứa team_id và danh sách interns + evaluations.
     * Ví dụ: [ { "team_id": 101, "interns": [ ... ] } ]
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamInfoDTO>> getTeamEvaluations(@PathVariable Integer teamId) {
        TeamInfoDTO dto = evaluationService.buildTeamInfo(teamId);
        return ResponseEntity.ok(Collections.singletonList(dto));
    }
}