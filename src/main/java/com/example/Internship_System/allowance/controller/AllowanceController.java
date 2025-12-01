package com.example.Internship_System.allowance.controller;

import com.example.Internship_System.allowance.dto.AllowanceDTO;
import com.example.Internship_System.allowance.dto.InternSearchDTO;
import com.example.Internship_System.allowance.dto.PaginatedAllowanceDTO;
import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.allowance.service.AllowanceService;
import com.example.Internship_System.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/allowances")
@CrossOrigin(origins = "*")
public class AllowanceController {
    @Autowired
    private AllowanceService allowanceService;

    @Autowired
    private NotificationService notificationService;

    //CREATE - Add new allowance
    @PostMapping
    public ResponseEntity<Allowance> createAllowance(@RequestBody Allowance allowance) {
        try {
            Allowance savedAllowance = allowanceService.save(allowance);
            // Send notification to intern
            notificationService.createAllowanceNotification(
                    savedAllowance.getInternId(),
                    savedAllowance.getType(),
                    savedAllowance.getAmount().toString()
            );
            return new ResponseEntity<>(savedAllowance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ all allowances with intern names and pagination info
    @GetMapping
    public ResponseEntity<?> getAllAllowances(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<AllowanceDTO> allowances = allowanceService.findAllWithInternNames();
            
            // Apply sorting if sortBy is specified
            if (sortBy != null && !sortBy.isEmpty()) {
                allowances = allowances.stream()
                        .sorted((a, b) -> {
                            int comparison;
                            switch (sortBy.toLowerCase()) {
                                case "allowanceid" -> comparison = Integer.compare(a.getAllowanceId(), b.getAllowanceId());
                                case "internid" -> comparison = Integer.compare(a.getInternId(), b.getInternId());
                                case "type" -> comparison = a.getType().compareTo(b.getType());
                                case "amount" -> comparison = a.getAmount().compareTo(b.getAmount());
                                case "dateapplied" -> comparison = a.getDateApplied().compareTo(b.getDateApplied());
                                default -> comparison = 0;
                            }
                            return "desc".equalsIgnoreCase(direction) ? -comparison : comparison;
                        })
                        .toList();
            }
            
            if (page != null && size != null) {
                int start = page * size;
                int end = Math.min(start + size, allowances.size());
                List<AllowanceDTO> paginatedAllowances = allowances.subList(start, end);
                PaginatedAllowanceDTO response = new PaginatedAllowanceDTO(paginatedAllowances, allowances.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allowances, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ allowance by id
    @GetMapping("/{id}")
    public ResponseEntity<Allowance> getAllowanceById(@PathVariable("id") int id) {
        Optional<Allowance> allowance = allowanceService.findById(id);
        return allowance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //READ allowances by intern id with intern name and pagination info
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getAllowancesByInternId(
            @PathVariable("internId") int internId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<AllowanceDTO> allowances = allowanceService.findByInternIdWithInternName(internId);
            
            if (page != null && size != null) {
                int start = page * size;
                int end = Math.min(start + size, allowances.size());
                List<AllowanceDTO> paginatedAllowances = allowances.subList(start, end);
                PaginatedAllowanceDTO response = new PaginatedAllowanceDTO(paginatedAllowances, allowances.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allowances, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //UPDATE allowance
    @PutMapping("/{id}")
    public ResponseEntity<Allowance> updateAllowance(@PathVariable("id") int id, @RequestBody Allowance allowance) {
        Optional<Allowance> existingAllowance = allowanceService.findById(id);
        if (existingAllowance.isPresent()) {
            Allowance allowanceToUpdate = existingAllowance.get();
            allowanceToUpdate.setInternId(allowance.getInternId());
            allowanceToUpdate.setType(allowance.getType());
            allowanceToUpdate.setAmount(allowance.getAmount());
            allowanceToUpdate.setDateApplied(allowance.getDateApplied());
            allowanceToUpdate.setNote(allowance.getNote());

            return new ResponseEntity<>(allowanceService.save(allowanceToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //DELETE allowance by id
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAllowance(@PathVariable("id") int id) {
        try {
            Optional<Allowance> allowance = allowanceService.findById(id);
            if (allowance.isPresent()) {
                allowanceService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //FILTER allowances with intern names and pagination info
    @GetMapping("/filter/search")
    public ResponseEntity<?> filterAllowances(
            @RequestParam(required = false) Integer internId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            LocalDate start = startDate != null && !startDate.isEmpty() ? LocalDate.parse(startDate) : null;
            LocalDate end = endDate != null && !endDate.isEmpty() ? LocalDate.parse(endDate) : null;
            
            List<AllowanceDTO> allAllowances = allowanceService.filterAllowancesWithInternNames(internId, type, minAmount, maxAmount, start, end);
            
            if (page != null && size != null) {
                int start_idx = page * size;
                int end_idx = Math.min(start_idx + size, allAllowances.size());
                List<AllowanceDTO> paginatedAllowances = allAllowances.subList(start_idx, end_idx);
                PaginatedAllowanceDTO response = new PaginatedAllowanceDTO(paginatedAllowances, allAllowances.size(), page, size);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allAllowances, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //SEARCH interns by name for allowance (returns only internId and fullName)
    @GetMapping("/search/interns")
    public ResponseEntity<?> searchInternsForAllowance(@RequestParam String name) {
        try {
            List<InternSearchDTO> interns = allowanceService.searchInternsForAllowance(name);
            return new ResponseEntity<>(interns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
