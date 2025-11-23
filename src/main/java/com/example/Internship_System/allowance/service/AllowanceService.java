package com.example.Internship_System.allowance.service;

import com.example.Internship_System.allowance.entity.Allowance;
import com.example.Internship_System.repository.AllowanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AllowanceService {
    @Autowired
    private AllowanceRepository repository;

    public Allowance save(Allowance allowance) {
        return repository.save(allowance);
    }

    public List<Allowance> findAll() {
        return repository.findAll();
    }

    public List<Allowance> findAllSorted(String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository.findAll(Sort.by(sortDirection, sortBy));
    }

    public Optional<Allowance> findById(int id) {
        return repository.findById(id);
    }

    public List<Allowance> findByInternId(int internId) {
        return repository.findByInternId(internId);
    }

    public List<Allowance> findByInternIdSorted(int internId, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<Allowance> allowances = repository.findByInternId(internId);
        return allowances.stream()
                .sorted((a, b) -> {
                    int comparison = compareByField(a, b, sortBy);
                    return sortDirection == Sort.Direction.DESC ? -comparison : comparison;
                })
                .toList();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    private int compareByField(Allowance a, Allowance b, String field) {
        return switch (field.toLowerCase()) {
            case "allowanceid" -> Integer.compare(a.getAllowanceId(), b.getAllowanceId());
            case "internid" -> Integer.compare(a.getInternId(), b.getInternId());
            case "type" -> a.getType().compareTo(b.getType());
            case "amount" -> a.getAmount().compareTo(b.getAmount());
            case "dateapplied" -> a.getDateApplied().compareTo(b.getDateApplied());
            case "note" -> a.getNote() != null ? a.getNote().compareTo(b.getNote() != null ? b.getNote() : "") : 0;
            default -> 0;
        };
    }
}
