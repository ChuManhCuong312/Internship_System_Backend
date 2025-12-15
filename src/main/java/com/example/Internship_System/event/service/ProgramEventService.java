package com.example.Internship_System.event.service;

import com.example.Internship_System.event.dto.ProgramEventRequest;
import com.example.Internship_System.event.entity.ProgramEvent;
import com.example.Internship_System.event.repository.ProgramEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.Internship_System.program.entity.Program;
import com.example.Internship_System.repository.ProgramRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ProgramEventService {

    private final ProgramEventRepository eventRepository;
    private final ProgramRepository programRepository;

    public ProgramEventService(
            ProgramEventRepository eventRepository,
            ProgramRepository programRepository
    ) {
        this.eventRepository = eventRepository;
        this.programRepository = programRepository;
    }

    public void create(ProgramEventRequest request) {

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time phải sau start time");
        }
        validateEventWithinProgram(
                request.getProgramId(),
                request.getEventDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (eventRepository.existsByProgramIdAndTitle(
                request.getProgramId(),
                request.getTitle()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Tên sự kiện đã tồn tại trong chương trình này"
            );
        }

        boolean hasConflict = !eventRepository
                .findOverlappingEvents(
                        request.getProgramId(),
                        request.getEventDate(),
                        request.getStartTime(),
                        request.getEndTime()
                ).isEmpty();

        if (hasConflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Sự kiện bị trùng thời gian với sự kiện khác"
            );
        }

        ProgramEvent event = new ProgramEvent(
                request.getProgramId(),
                request.getTitle(),
                request.getLocation(),
                request.getEventDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDescription()
        );

        eventRepository.save(event);
    }

    public void update(Integer eventId, ProgramEventRequest request) {

        ProgramEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event không tồn tại"));

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time phải sau start time");
        }
        validateEventWithinProgram(
                request.getProgramId(),
                request.getEventDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (eventRepository.existsByProgramIdAndTitleAndEventIdNot(
                request.getProgramId(),
                request.getTitle(),
                eventId
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Tên sự kiện đã tồn tại trong chương trình này"
            );
        }

        boolean hasConflict = !eventRepository
                .findOverlappingEventsExcludeSelf(
                        eventId,
                        request.getProgramId(),
                        request.getEventDate(),
                        request.getStartTime(),
                        request.getEndTime()
                ).isEmpty();

        if (hasConflict) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Sự kiện bị trùng thời gian với sự kiện khác"
            );
        }

        event.setProgramId(request.getProgramId());
        event.setTitle(request.getTitle());
        event.setLocation(request.getLocation());
        event.setEventDate(request.getEventDate());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setDescription(request.getDescription());

        eventRepository.save(event);
    }

    public void delete(Integer eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Event không tồn tại");
        }

        eventRepository.deleteById(eventId);
    }
    public List<ProgramEvent> getByProgramId(Integer programId) {
        return eventRepository.findByProgramId(programId);
    }
    private void validateEventWithinProgram(
            Integer programId,
            LocalDate eventDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Program không tồn tại"
                        )
                );

        LocalDateTime eventStart = LocalDateTime.of(eventDate, startTime);
        LocalDateTime eventEnd   = LocalDateTime.of(eventDate, endTime);

        if (eventStart.isBefore(program.getStartDate())
                || eventEnd.isAfter(program.getEndDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Thời gian sự kiện phải nằm trong thời gian của chương trình"
            );
        }
    }

}
