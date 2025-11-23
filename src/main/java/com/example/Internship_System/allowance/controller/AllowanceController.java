package com.example.Internship_System.allowance.controller;

import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.allowance.service.AllowanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/allowances")
@CrossOrigin(origins = "*")
public class AllowanceController {
    @Autowired
    private AllowanceService allowanceService;

    //CREATE - Add new allowance
    @PostMapping
    public ResponseEntity<Allowance> createAllowance(@RequestBody Allowance allowance) {
        try {
            Allowance savedAllowance = allowanceService.save(allowance);
            return new ResponseEntity<>(savedAllowance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ all allowances
    @GetMapping
    public ResponseEntity<List<Allowance>> getAllAllowances(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        try {
            List<Allowance> allowances;
            if (sortBy != null && !sortBy.isEmpty()) {
                allowances = allowanceService.findAllSorted(sortBy, direction);
            } else {
                allowances = allowanceService.findAll();
            }
            return new ResponseEntity<>(allowances, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ all allowances with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Allowance>> getAllAllowancesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "allowanceId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        try {
            Page<Allowance> allowances = allowanceService.findAllPaginated(page, size, sortBy, direction);
            return new ResponseEntity<>(allowances, HttpStatus.OK);
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
    public ResponseEntity<List<Allowance>> getAllowancesByInternId(
            @PathVariable("internId") int internId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        try {
            List<Allowance> allowances;
            if (sortBy != null && !sortBy.isEmpty()) {
                allowances = allowanceService.findByInternIdSorted(internId, sortBy, direction);
            } else {
                allowances = allowanceService.findByInternId(internId);
            }
            return new ResponseEntity<>(allowances, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //READ allowances by intern id with pagination
    @GetMapping("/intern/{internId}/paginated")
    public ResponseEntity<Page<Allowance>> getAllowancesByInternIdPaginated(
            @PathVariable("internId") int internId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "allowanceId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        try {
            Page<Allowance> allowances = allowanceService.findByInternIdPaginated(internId, page, size, sortBy, direction);
            return new ResponseEntity<>(allowances, HttpStatus.OK);
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
}
