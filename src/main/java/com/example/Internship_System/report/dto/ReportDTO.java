package com.example.Internship_System.report.dto;

import java.util.List;

public class ReportDTO {

    private Integer programId;
    private String programName;
    private String department;
    private List<InternReportDTO> interns;
    private Integer totalInterns;
    private Integer internsWithEvaluations;
    private Double avgFinalScore;

    public ReportDTO(Integer programId, String programName, String department, List<InternReportDTO> interns) {
        this(programId, programName, department, interns, null, null, null);
    }

    public ReportDTO(Integer programId,
                     String programName,
                     String department,
                     List<InternReportDTO> interns,
                     Integer totalInterns,
                     Integer internsWithEvaluations,
                     Double avgFinalScore) {
        this.programId = programId;
        this.programName = programName;
        this.department = department;
        this.interns = interns;
        this.totalInterns = totalInterns;
        this.internsWithEvaluations = internsWithEvaluations;
        this.avgFinalScore = avgFinalScore;
    }

    public Integer getProgramId() {
        return programId;
    }

    public String getProgramName() {
        return programName;
    }

    public String getDepartment() {
        return department;
    }

    public List<InternReportDTO> getInterns() {
        return interns;
    }

    public Integer getTotalInterns() {
        return totalInterns;
    }

    public Integer getInternsWithEvaluations() {
        return internsWithEvaluations;
    }

    public Double getAvgFinalScore() {
        return avgFinalScore;
    }

    public static class InternReportDTO {
        private Integer internId;
        private String fullName;
        private String email;
        private String phone;
        private String school;
        private String major;
        private Integer teamId;
        private String teamName;
        private String mentorName;
        private Integer evaluationCount;
        private Double avgTechnical;
        private Double avgCommunication;
        private Double avgDiscipline;
        private Double avgAttitude;
        private Double finalScore;

        public InternReportDTO(Integer internId,
                               String fullName,
                               String email,
                               String phone,
                               String school,
                               String major,
                               Integer teamId,
                               String teamName,
                               String mentorName,
                               Integer evaluationCount,
                               Double avgTechnical,
                               Double avgCommunication,
                               Double avgDiscipline,
                               Double avgAttitude,
                               Double finalScore) {
            this.internId = internId;
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.school = school;
            this.major = major;
            this.teamId = teamId;
            this.teamName = teamName;
            this.mentorName = mentorName;
            this.evaluationCount = evaluationCount;
            this.avgTechnical = avgTechnical;
            this.avgCommunication = avgCommunication;
            this.avgDiscipline = avgDiscipline;
            this.avgAttitude = avgAttitude;
            this.finalScore = finalScore;
        }

        public Integer getInternId() {
            return internId;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getSchool() {
            return school;
        }

        public String getMajor() {
            return major;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public String getTeamName() {
            return teamName;
        }

        public String getMentorName() {
            return mentorName;
        }

        public Integer getEvaluationCount() {
            return evaluationCount;
        }

        public Double getAvgTechnical() {
            return avgTechnical;
        }

        public Double getAvgCommunication() {
            return avgCommunication;
        }

        public Double getAvgDiscipline() {
            return avgDiscipline;
        }

        public Double getAvgAttitude() {
            return avgAttitude;
        }

        public Double getFinalScore() {
            return finalScore;
        }
    }
}
