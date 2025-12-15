package com.example.Internship_System.event.service;

import com.example.Internship_System.event.dto.ProgramEventRequest;
import com.example.Internship_System.event.entity.ProgramEvent;
import com.example.Internship_System.event.repository.ProgramEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProgramEventService {

    private final ProgramEventRepository eventRepository;

    public ProgramEventService(ProgramEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void create(ProgramEventRequest request) {

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time phải sau start time");
        }

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
}
