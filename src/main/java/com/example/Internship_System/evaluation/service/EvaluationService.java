
package com.example.Internship_System.evaluation.service;

import com.example.Internship_System.evaluation.DTO.EvaluationRequest;
import com.example.Internship_System.evaluation.entity.Evaluation;
import com.example.Internship_System.mentor.entity.MentorUser;
import com.example.Internship_System.repository.EvaluationRepository;
import com.example.Internship_System.repository.TeamInternRepository;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.team.entity.TeamIntern;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.repository.MentorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationService {

    private final TeamInternRepository teamInternRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final InternRepository internRepository;
    private final MentorRepository mentorRepository;

    public EvaluationService(TeamInternRepository teamInternRepository,
                             EvaluationRepository evaluationRepository,
                             UserRepository userRepository,
                             InternRepository internRepository,
                             MentorRepository mentorRepository) {
        this.teamInternRepository = teamInternRepository;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.internRepository = internRepository;
        this.mentorRepository = mentorRepository;
    }

    public TeamInfoDTO buildTeamInfo(Integer teamId) {
        List<TeamIntern> teamInterns = teamInternRepository.findByTeam_TeamId(teamId);
        if (teamInterns == null || teamInterns.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy team hoặc team chưa có intern.");
        }

        TeamInfoDTO teamInfo = new TeamInfoDTO(teamId, new ArrayList<>());

        for (TeamIntern ti : teamInterns) {
            InternProfile intern = ti.getIntern();
            if (intern == null) continue;

            // Lấy thông tin User từ intern.userId
            String name = null, email = null, phone = null;
            if (intern.getUserId() != null) {
                User user = userRepository.findById(intern.getUserId()).orElse(null);
                if (user != null) {
                    name = user.getFullName();
                    email = user.getEmail();
                    phone = user.getPhone();
                }
            }

            InternDTO internDTO = new InternDTO(
                    intern.getInternId(),
                    name,
                    email,
                    phone,
                    new ArrayList<>()
            );

            // Lấy evaluations cho intern này
            List<Evaluation> evaluations = evaluationRepository.findByIntern_InternId(intern.getInternId());
            DateTimeFormatter dateFmt = DateTimeFormatter.ISO_DATE; // "yyyy-MM-dd"

            for (Evaluation e : evaluations) {
                String createdDate = (e.getCreatedAt() != null)
                        ? e.getCreatedAt().toLocalDate().format(dateFmt)
                        : null;

                EvaluationDTO evalDTO = new EvaluationDTO(
                        e.getEvaluationId(),
                        e.getTitle(),
                        e.getTechnical(),      // Double
                        e.getCommunication(),  // Double
                        e.getDiscipline(),     // Double
                        e.getAttitude(),       // Double
                        e.getWeight(),
                        e.getNote(),
                        createdDate
                );
                internDTO.getEvaluations().add(evalDTO);
            }

            teamInfo.getInterns().add(internDTO);
        }

        return teamInfo;
    }

    // ===== DTO classes =====

    public static class TeamInfoDTO {
        private Integer team_id;
        private List<InternDTO> interns;

        public TeamInfoDTO(Integer team_id, List<InternDTO> interns) {
            this.team_id = team_id;
            this.interns = interns;
        }
        public Integer getTeam_id() { return team_id; }
        public List<InternDTO> getInterns() { return interns; }
    }

    public static class InternDTO {
        private Integer intern_id;
        private String intern_name;
        private String email;
        private String phone;
        private List<EvaluationDTO> evaluations;

        public InternDTO(Integer intern_id, String intern_name, String email, String phone, List<EvaluationDTO> evaluations) {
            this.intern_id = intern_id;
            this.intern_name = intern_name;
            this.email = email;
            this.phone = phone;
            this.evaluations = evaluations;
        }

        public Integer getIntern_id() { return intern_id; }
        public String getIntern_name() { return intern_name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public List<EvaluationDTO> getEvaluations() { return evaluations; }
    }

    public static class EvaluationDTO {
        private Integer evaluation_id;
        private String title;
        private Double technical;      // Đã đổi sang Double
        private Double communication;  // Đã đổi sang Double
        private Double discipline;     // Đã đổi sang Double
        private Double attitude;       // Đã đổi sang Double
        private Integer weight;
        private String note;
        private String created_at; // "yyyy-MM-dd"

        public EvaluationDTO(Integer evaluation_id, String title, Double technical, Double communication,
                             Double discipline, Double attitude, Integer weight, String note, String created_at) {
            this.evaluation_id = evaluation_id;
            this.title = title;
            this.technical = technical;
            this.communication = communication;
            this.discipline = discipline;
            this.attitude = attitude;
            this.weight = weight;
            this.note = note;
            this.created_at = created_at;
        }

        public Integer getEvaluation_id() { return evaluation_id; }
        public String getTitle() { return title; }
        public Double getTechnical() { return technical; }
        public Double getCommunication() { return communication; }
        public Double getDiscipline() { return discipline; }
        public Double getAttitude() { return attitude; }
        public Integer getWeight() { return weight; }
        public String getNote() { return note; }
        public String getCreated_at() { return created_at; }
    }

    public EvaluationDTO createEvaluation(EvaluationRequest req) {

        InternProfile intern = internRepository.findById(req.getInternId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intern không tồn tại"));

        MentorUser mentor = mentorRepository.findById(req.getMentorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor không tồn tại"));

        // Tổng weight hiện tại của intern
        Integer currentTotal = evaluationRepository.sumWeightByInternId(req.getInternId());
        int newWeight = req.getWeight() != null ? req.getWeight() : 0;

        if (currentTotal + newWeight > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Tổng hệ số hiện tại (%d) + hệ số mới (%d) vượt quá 100", currentTotal, newWeight)
            );
        }

        Evaluation e = new Evaluation();
        e.setIntern(intern);
        e.setMentorEvaluate(mentor);
        e.setTitle(req.getTitle());
        e.setTechnical(req.getTechnical());
        e.setCommunication(req.getCommunication());
        e.setDiscipline(req.getDiscipline());
        e.setAttitude(req.getAttitude());
        e.setWeight(req.getWeight());
        e.setNote(req.getNote());

        evaluationRepository.save(e);

        return toDTO(e);
    }
    public EvaluationDTO updateEvaluation(Integer id, EvaluationRequest req) {

        Evaluation e = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation không tồn tại"));

        if (req.getInternId() != null) {
            InternProfile intern = internRepository.findById(req.getInternId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Intern không tồn tại"));
            e.setIntern(intern);
        }

        if (req.getMentorId() != null) {
            MentorUser mentor = mentorRepository.findById(req.getMentorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor không tồn tại"));
            e.setMentorEvaluate(mentor);
        }
        Integer sumOtherWeights = evaluationRepository
                .sumWeightByInternExcludeCurrent(req.getInternId(), e.getEvaluationId());
        if (sumOtherWeights == null) sumOtherWeights = 0;
        Integer newWeight = (req.getWeight() != null) ? req.getWeight() : 0;
        int totalAfterUpdate = sumOtherWeights + newWeight;
        if (totalAfterUpdate > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Tổng hệ số sau cập nhật là %d, vượt quá 100",
                            totalAfterUpdate)
            );
        }

        e.setTitle(req.getTitle());
        e.setTechnical(req.getTechnical());
        e.setCommunication(req.getCommunication());
        e.setDiscipline(req.getDiscipline());
        e.setAttitude(req.getAttitude());
        e.setWeight(req.getWeight());
        e.setNote(req.getNote());

        evaluationRepository.save(e);

        return toDTO(e);
    }
    public void deleteEvaluation(Integer id) {
        if (!evaluationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation không tồn tại");
        }
        evaluationRepository.deleteById(id);
    }
    public EvaluationDTO getById(Integer id) {
        Evaluation e = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation không tồn tại"));
        return toDTO(e);
    }
    private EvaluationDTO toDTO(Evaluation e) {
        return new EvaluationDTO(
                e.getEvaluationId(),
                e.getTitle(),
                e.getTechnical(),
                e.getCommunication(),
                e.getDiscipline(),
                e.getAttitude(),
                e.getWeight(),
                e.getNote(),
                e.getCreatedAt() != null ? e.getCreatedAt().toLocalDate().toString() : null
        );
    }

}
