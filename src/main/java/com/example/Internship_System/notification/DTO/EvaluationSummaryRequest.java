package com.example.Internship_System.notification.DTO;

import com.example.Internship_System.evaluation.service.EvaluationService;

import java.util.List;

public class EvaluationSummaryRequest {

    private Integer team_id;
    private List<InternData> interns;

    public EvaluationSummaryRequest() {}

    public Integer getTeam_id() { return team_id; }
    public void setTeam_id(Integer team_id) { this.team_id = team_id; }

    public List<InternData> getInterns() { return interns; }
    public void setInterns(List<InternData> interns) { this.interns = interns; }

    public static class InternData {
        private Integer intern_id;
        private String intern_name;
        private String email;
        private String phone;
        private List<EvaluationService.EvaluationDTO> evaluations;

        public InternData() {}

        public Integer getIntern_id() { return intern_id; }
        public void setIntern_id(Integer intern_id) { this.intern_id = intern_id; }

        public String getIntern_name() { return intern_name; }
        public void setIntern_name(String intern_name) { this.intern_name = intern_name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public List<EvaluationService.EvaluationDTO> getEvaluations() { return evaluations; }
        public void setEvaluations(List<EvaluationService.EvaluationDTO> evaluations) { this.evaluations = evaluations; }
    }
}

