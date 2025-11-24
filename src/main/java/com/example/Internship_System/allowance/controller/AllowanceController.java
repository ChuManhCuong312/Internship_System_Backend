package com.example.Internship_System.allowance.controller;

import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.allowance.service.AllowanceService;
import com.example.Internship_System.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    //READ all allowances
    @GetMapping
    public ResponseEntity<?> getAllAllowances(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            if (page != null && size != null) {
                Page<Allowance> allowances;
                if (sortBy != null && !sortBy.isEmpty()) {
                    allowances = allowanceService.findAllPaginated(page, size, sortBy, direction);
                } else {
                    allowances = allowanceService.findAllPaginatedNoSort(page, size);
                }
                return new ResponseEntity<>(allowances, HttpStatus.OK);
            } else {
                List<Allowance> allowances;
                if (sortBy != null && !sortBy.isEmpty()) {
                    allowances = allowanceService.findAllSorted(sortBy, direction);
                } else {
                    allowances = allowanceService.findAll();
                }
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

    //READ allowances by intern id
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getAllowancesByInternId(
            @PathVariable("internId") int internId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            if (page != null && size != null) {
                List<Allowance> allAllowances;
                if (sortBy != null && !sortBy.isEmpty()) {
                    allAllowances = allowanceService.findByInternIdSorted(internId, sortBy, direction);
                } else {
                    allAllowances = allowanceService.findByInternId(internId);
                }
                int start = page * size;
                int end = Math.min(start + size, allAllowances.size());
                List<Allowance> paginatedAllowances = allAllowances.subList(start, end);
                return new ResponseEntity<>(paginatedAllowances, HttpStatus.OK);
            } else {
                List<Allowance> allowances;
                if (sortBy != null && !sortBy.isEmpty()) {
                    allowances = allowanceService.findByInternIdSorted(internId, sortBy, direction);
                } else {
                    allowances = allowanceService.findByInternId(internId);
                }
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

    //FILTER allowances
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
            
            List<Allowance> allAllowances = allowanceService.filterAllowances(internId, type, minAmount, maxAmount, start, end);
            
            if (page != null && size != null) {
                int start_idx = page * size;
                int end_idx = Math.min(start_idx + size, allAllowances.size());
                List<Allowance> paginatedAllowances = allAllowances.subList(start_idx, end_idx);
                return new ResponseEntity<>(paginatedAllowances, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allAllowances, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
