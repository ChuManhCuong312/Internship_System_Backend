package com.example.Internship_System.report.controller;

import com.example.Internship_System.report.dto.ReportDTO;
import com.example.Internship_System.report.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/program/{programId}/final-evaluations")
    public ResponseEntity<ReportDTO> getFinalEvaluationReportByProgram(
            @PathVariable Integer programId,
            @RequestParam(value = "teamId", required = false) Integer teamId
    ) {
        ReportDTO report = (teamId != null)
                ? reportService.getFinalEvaluationReportByProgramAndTeam(programId, teamId)
                : reportService.getFinalEvaluationReportByProgram(programId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/program/{programId}/final-evaluations/export")
    public ResponseEntity<byte[]> exportFinalEvaluationReportByProgram(
            @PathVariable Integer programId,
            @RequestParam(value = "teamId", required = false) Integer teamId
    ) {
        byte[] data = reportService.exportFinalEvaluationReportForProgram(programId, teamId);
        String filename = "final_evaluations_program_" + programId + (teamId != null ? "_team_" + teamId : "") + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/team/{teamId}/final-evaluations")
    public ResponseEntity<ReportDTO> getFinalEvaluationReportByTeam(@PathVariable Integer teamId) {
        ReportDTO report = reportService.getFinalEvaluationReportByTeam(teamId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/team/{teamId}/final-evaluations/export")
    public ResponseEntity<byte[]> exportFinalEvaluationReportByTeam(@PathVariable Integer teamId) {
        byte[] data = reportService.exportFinalEvaluationReportForTeam(teamId);
        String filename = "final_evaluations_team_" + teamId + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }
}
