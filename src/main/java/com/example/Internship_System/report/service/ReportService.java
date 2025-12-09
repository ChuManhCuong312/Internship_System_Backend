package com.example.Internship_System.report.service;

import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.evaluation.entity.Evaluation;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.report.dto.ReportDTO;
import com.example.Internship_System.repository.EvaluationRepository;
import com.example.Internship_System.repository.ProgramRepository;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.repository.TeamRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.team.entity.Team;
import com.example.Internship_System.team.entity.TeamIntern;
import com.example.Internship_System.intern.entity.InternProfile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final ProgramRepository programRepository;
    private final TeamRepository teamRepository;
    private final TeamInternRepository teamInternRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;

    public ReportService(ProgramRepository programRepository,
                         TeamRepository teamRepository,
                         TeamInternRepository teamInternRepository,
                         EvaluationRepository evaluationRepository,
                         UserRepository userRepository) {
        this.programRepository = programRepository;
        this.teamRepository = teamRepository;
        this.teamInternRepository = teamInternRepository;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
    }

    public ReportDTO getFinalEvaluationReportByProgram(Integer programId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chương trình không tồn tại"));

        List<Team> teams = teamRepository.findByProgramProgramId(programId);
        List<ReportDTO.InternReportDTO> internReports = buildInternReportsForTeams(teams, program);

        return buildReportDTO(program, internReports);
    }

    public byte[] exportFinalEvaluationReportForProgram(Integer programId, Integer teamId) {
        ReportDTO report = (teamId != null)
                ? getFinalEvaluationReportByProgramAndTeam(programId, teamId)
                : getFinalEvaluationReportByProgram(programId);
        return buildExcel(report);
    }

    public byte[] exportFinalEvaluationReportForTeam(Integer teamId) {
        ReportDTO report = getFinalEvaluationReportByTeam(teamId);
        return buildExcel(report);
    }

    public ReportDTO getFinalEvaluationReportByProgramAndTeam(Integer programId, Integer teamId) {
        if (!teamRepository.existsByTeamIdAndProgram_ProgramId(teamId, programId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team không thuộc chương trình này");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team không tồn tại"));

        Program program = team.getProgram();
        List<Team> teams = new ArrayList<>();
        teams.add(team);

        List<ReportDTO.InternReportDTO> internReports = buildInternReportsForTeams(teams, program);

        return buildReportDTO(program, internReports);
    }

    public ReportDTO getFinalEvaluationReportByTeam(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team không tồn tại"));

        Program program = team.getProgram();
        List<Team> teams = new ArrayList<>();
        teams.add(team);

        List<ReportDTO.InternReportDTO> internReports = buildInternReportsForTeams(teams, program);

        return buildReportDTO(program, internReports);
    }

    private Map<Integer, String> buildTeamNameMap(Program program) {
        List<Team> allTeams = teamRepository.findByProgramProgramId(program.getProgramId());
        allTeams.sort(Comparator.comparing(Team::getTeamId));

        Map<Integer, String> teamNameMap = new HashMap<>();
        int index = 1;
        for (Team t : allTeams) {
            teamNameMap.put(t.getTeamId(), "Team " + index++);
        }
        return teamNameMap;
    }

    private List<ReportDTO.InternReportDTO> buildInternReportsForTeams(List<Team> teams, Program program) {
        List<ReportDTO.InternReportDTO> internReports = new ArrayList<>();

        Map<Integer, String> teamNameMap = buildTeamNameMap(program);

        for (Team team : teams) {
            Integer teamId = team.getTeamId();
            String teamName = teamNameMap.get(teamId);
            String mentorName = null;
            if (team.getMentor() != null && team.getMentor().getUser() != null) {
                mentorName = team.getMentor().getUser().getFullName();
            }

            List<TeamIntern> teamInterns = teamInternRepository.findByTeam_TeamId(teamId);
            for (TeamIntern ti : teamInterns) {
                InternProfile intern = ti.getIntern();
                if (intern == null) {
                    continue;
                }

                User user = null;
                if (intern.getUserId() != null) {
                    user = userRepository.findById(intern.getUserId()).orElse(null);
                }

                String fullName = user != null ? user.getFullName() : null;
                String email = user != null ? user.getEmail() : null;
                String phone = user != null ? user.getPhone() : null;

                List<Evaluation> evaluations = evaluationRepository.findByIntern_InternId(intern.getInternId());
                if (evaluations == null || evaluations.isEmpty()) {
                    ReportDTO.InternReportDTO dto = new ReportDTO.InternReportDTO(
                            intern.getInternId(),
                            fullName,
                            email,
                            phone,
                            intern.getSchool(),
                            intern.getMajor(),
                            teamId,
                            teamName,
                            mentorName,
                            0,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    internReports.add(dto);
                    continue;
                }

                int evalCount = evaluations.size();

                double weightedTechnical = 0.0;
                double weightedCommunication = 0.0;
                double weightedDiscipline = 0.0;
                double weightedAttitude = 0.0;
                double weightedTotalScore = 0.0;

                for (Evaluation e : evaluations) {
                    int weightInt = e.getWeight() != null ? e.getWeight() : 0;
                    double w = weightInt / 100.0; // giống FE: weight (%) / 100

                    Double technicalObj = e.getTechnical();
                    Double communicationObj = e.getCommunication();
                    Double disciplineObj = e.getDiscipline();
                    Double attitudeObj = e.getAttitude();

                    double technical = technicalObj != null ? technicalObj : 0.0;
                    double communication = communicationObj != null ? communicationObj : 0.0;
                    double discipline = disciplineObj != null ? disciplineObj : 0.0;
                    double attitude = attitudeObj != null ? attitudeObj : 0.0;

                    weightedTechnical += technical * w;
                    weightedCommunication += communication * w;
                    weightedDiscipline += discipline * w;
                    weightedAttitude += attitude * w;

                    double evalAverage = (technical + communication + discipline + attitude) / 4.0;
                    weightedTotalScore += evalAverage * w;
                }

                Double avgTechnical = evaluations.isEmpty() ? null : weightedTechnical;
                Double avgCommunication = evaluations.isEmpty() ? null : weightedCommunication;
                Double avgDiscipline = evaluations.isEmpty() ? null : weightedDiscipline;
                Double avgAttitude = evaluations.isEmpty() ? null : weightedAttitude;
                Double finalScore = evaluations.isEmpty() ? null : weightedTotalScore;

                ReportDTO.InternReportDTO dto = new ReportDTO.InternReportDTO(
                        intern.getInternId(),
                        fullName,
                        email,
                        phone,
                        intern.getSchool(),
                        intern.getMajor(),
                        teamId,
                        teamName,
                        mentorName,
                        evalCount,
                        avgTechnical,
                        avgCommunication,
                        avgDiscipline,
                        avgAttitude,
                        finalScore
                );
                internReports.add(dto);
            }
        }

        return internReports;
    }

    private ReportDTO buildReportDTO(Program program, List<ReportDTO.InternReportDTO> internReports) {
        int totalInterns = internReports.size();
        int internsWithEvaluations = 0;
        double sumFinalScore = 0.0;
        int countFinalScore = 0;

        for (ReportDTO.InternReportDTO intern : internReports) {
            if (intern.getEvaluationCount() != null && intern.getEvaluationCount() > 0) {
                internsWithEvaluations++;
            }
            if (intern.getFinalScore() != null) {
                sumFinalScore += intern.getFinalScore();
                countFinalScore++;
            }
        }

        Double avgFinalScore = countFinalScore > 0 ? (sumFinalScore / countFinalScore) : null;

        return new ReportDTO(
                program.getProgramId(),
                program.getName(),
                program.getDepartment(),
                internReports,
                totalInterns,
                internsWithEvaluations,
                avgFinalScore
        );
    }

    private byte[] buildExcel(ReportDTO report) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Final Report");

            int rowIdx = 0;

            Row programRow = sheet.createRow(rowIdx++);
            createCell(programRow, 0, "Program ID");
            createCell(programRow, 1, report.getProgramId() != null ? report.getProgramId().toString() : "");
            createCell(programRow, 2, "Program Name");
            createCell(programRow, 3, report.getProgramName() != null ? report.getProgramName() : "");
            createCell(programRow, 4, "Department");
            createCell(programRow, 5, report.getDepartment() != null ? report.getDepartment() : "");

            Row overviewRow = sheet.createRow(rowIdx++);
            createCell(overviewRow, 0, "Total Interns");
            createCell(overviewRow, 1, report.getTotalInterns() != null ? report.getTotalInterns().toString() : "");
            createCell(overviewRow, 2, "Interns With Evaluations");
            createCell(overviewRow, 3, report.getInternsWithEvaluations() != null ? report.getInternsWithEvaluations().toString() : "");
            createCell(overviewRow, 4, "Avg Final Score");
            createCell(overviewRow, 5, report.getAvgFinalScore() != null ? report.getAvgFinalScore().toString() : "");

            rowIdx++;

            Row headerRow = sheet.createRow(rowIdx++);
            int col = 0;
            createCell(headerRow, col++, "Intern ID");
            createCell(headerRow, col++, "Full Name");
            createCell(headerRow, col++, "Email");
            createCell(headerRow, col++, "Phone");
            createCell(headerRow, col++, "School");
            createCell(headerRow, col++, "Major");
            createCell(headerRow, col++, "Team");
            createCell(headerRow, col++, "Mentor Name");
            createCell(headerRow, col++, "Evaluation Count");
            createCell(headerRow, col++, "Avg Technical");
            createCell(headerRow, col++, "Avg Communication");
            createCell(headerRow, col++, "Avg Discipline");
            createCell(headerRow, col++, "Avg Attitude");
            createCell(headerRow, col, "Final Score");

            if (report.getInterns() != null) {
                for (ReportDTO.InternReportDTO intern : report.getInterns()) {
                    Row row = sheet.createRow(rowIdx++);
                    int c = 0;
                    createCell(row, c++, intern.getInternId() != null ? intern.getInternId().toString() : "");
                    createCell(row, c++, intern.getFullName() != null ? intern.getFullName() : "");
                    createCell(row, c++, intern.getEmail() != null ? intern.getEmail() : "");
                    createCell(row, c++, intern.getPhone() != null ? intern.getPhone() : "");
                    createCell(row, c++, intern.getSchool() != null ? intern.getSchool() : "");
                    createCell(row, c++, intern.getMajor() != null ? intern.getMajor() : "");
                    createCell(row, c++, intern.getTeamName() != null ? intern.getTeamName() : "");
                    createCell(row, c++, intern.getMentorName() != null ? intern.getMentorName() : "");
                    createCell(row, c++, intern.getEvaluationCount() != null ? intern.getEvaluationCount().toString() : "");
                    createCell(row, c++, intern.getAvgTechnical() != null ? intern.getAvgTechnical().toString() : "");
                    createCell(row, c++, intern.getAvgCommunication() != null ? intern.getAvgCommunication().toString() : "");
                    createCell(row, c++, intern.getAvgDiscipline() != null ? intern.getAvgDiscipline().toString() : "");
                    createCell(row, c++, intern.getAvgAttitude() != null ? intern.getAvgAttitude().toString() : "");
                    createCell(row, c, intern.getFinalScore() != null ? intern.getFinalScore().toString() : "");
                }
            }

            for (int i = 0; i <= 13; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể xuất Excel", e);
        }
    }

    private void createCell(Row row, int columnIndex, String value) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value : "");
    }
}
