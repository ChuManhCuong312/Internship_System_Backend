package com.example.Internship_System.intern.controller;

import com.example.Internship_System.intern.entity.ContractDocument;
import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import com.example.Internship_System.intern.service.ContractDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
public class ContractDocumentController {

    @Autowired
    private ContractDocumentService contractService;

    /**
     * CREATE - Add new contract document
     */
    @PostMapping
    public ResponseEntity<ContractDocument> createContract(@RequestBody ContractDocument contract) {
        try {
            ContractDocument savedContract = contractService.save(contract);
            return new ResponseEntity<>(savedContract, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ - Get all contract documents
     */
    @GetMapping
    public ResponseEntity<List<ContractDocument>> getAllContracts() {
        try {
            List<ContractDocument> contracts = contractService.findAll();
            if (contracts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(contracts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ - Get contract by document ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractDocument> getContractById(@PathVariable("id") int id) {
        Optional<ContractDocument> contract = contractService.findById(id);
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * READ - Get contract by intern ID
     */
    @GetMapping("/intern/{internId}")
    public ResponseEntity<ContractDocument> getContractByInternId(@PathVariable("internId") int internId) {
        Optional<ContractDocument> contract = contractService.findByInternId(internId);
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * READ - Get contracts by contract status
     * Use query parameter instead of path variable to handle enum values
     */
    @GetMapping("/by-contract-status")
    public ResponseEntity<?> getContractsByStatus(@RequestParam("status") String statusStr) {
        try {
            ContractStatus status = ContractStatus.valueOf(statusStr.toUpperCase().replace(" ", "_"));
            List<ContractDocument> contracts = contractService.findByContractStatus(status);
            if (contracts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(contracts, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid contract status. Valid values: NOT_UPLOAD, UPLOAD");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * READ - Get contracts by intern confirm status
     * Use query parameter instead of path variable to handle enum values
     */
    @GetMapping("/by-confirm-status")
    public ResponseEntity<?> getContractsByInternConfirmStatus(@RequestParam("status") String statusStr) {
        try {
            InternConfirmStatus status = InternConfirmStatus.valueOf(statusStr.toUpperCase().replace(" ", "_"));
            List<ContractDocument> contracts = contractService.findByInternConfirmStatus(status);
            if (contracts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(contracts, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid confirm status. Valid values: APPROVED, PENDING");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * UPDATE - Full update of contract document
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractDocument> updateContract(
            @PathVariable("id") int id,
            @RequestBody ContractDocument contract) {
        Optional<ContractDocument> existingContract = contractService.findById(id);

        if (existingContract.isPresent()) {
            ContractDocument contractToUpdate = existingContract.get();
            contractToUpdate.setIntern(contract.getIntern());
            contractToUpdate.setFilePath(contract.getFilePath());
            contractToUpdate.setContractStatus(contract.getContractStatus());
            contractToUpdate.setInternConfirmStatus(contract.getInternConfirmStatus());
            contractToUpdate.setConfirmAt(contract.getConfirmAt());
            contractToUpdate.setNote(contract.getNote());

            return new ResponseEntity<>(contractService.save(contractToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PATCH - Partial update of contract document
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ContractDocument> partialUpdateContract(
            @PathVariable("id") int id,
            @RequestBody ContractDocument contract) {
        Optional<ContractDocument> existingContract = contractService.findById(id);

        if (existingContract.isPresent()) {
            ContractDocument contractToUpdate = existingContract.get();

            if (contract.getIntern() != null) {
                contractToUpdate.setIntern(contract.getIntern());
            }
            if (contract.getFilePath() != null) {
                contractToUpdate.setFilePath(contract.getFilePath());
            }
            if (contract.getContractStatus() != null) {
                contractToUpdate.setContractStatus(contract.getContractStatus());
            }
            if (contract.getInternConfirmStatus() != null) {
                contractToUpdate.setInternConfirmStatus(contract.getInternConfirmStatus());
            }
            if (contract.getConfirmAt() != null) {
                contractToUpdate.setConfirmAt(contract.getConfirmAt());
            }
            if (contract.getNote() != null) {
                contractToUpdate.setNote(contract.getNote());
            }

            return new ResponseEntity<>(contractService.save(contractToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PATCH - Update contract status
     * Accepts status as string and converts to enum
     */
    @PatchMapping("/{id}/contract-status")
    public ResponseEntity<Map<String, Object>> updateContractStatus(
            @PathVariable("id") int id,
            @RequestParam String status) {
        try {
            ContractStatus contractStatus = ContractStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            ContractDocument updated = contractService.updateContractStatus(id, contractStatus);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contract status updated successfully");
            response.put("contract", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid contract status. Valid values: NOT_UPLOAD, UPLOAD");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * PATCH - Update intern confirm status
     * Accepts status as string and converts to enum
     */
    @PatchMapping("/{id}/confirm-status")
    public ResponseEntity<Map<String, Object>> updateInternConfirmStatus(
            @PathVariable("id") int id,
            @RequestParam String status,
            @RequestParam(required = false) String note) {
        try {
            InternConfirmStatus confirmStatus = InternConfirmStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            ContractDocument updated = contractService.updateInternConfirmStatus(id, confirmStatus, note);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Intern confirm status updated successfully");
            response.put("contract", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid confirm status. Valid values: APPROVED, PENDING");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * DELETE - Delete contract by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteContract(@PathVariable("id") int id) {
        try {
            Optional<ContractDocument> contract = contractService.findById(id);
            if (contract.isPresent()) {
                contractService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Statistics - Get contract statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getContractStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", contractService.countAll());
            stats.put("uploaded", contractService.countByContractStatus(ContractStatus.UPLOAD));
            stats.put("notUploaded", contractService.countByContractStatus(ContractStatus.NOT_UPLOAD));
            stats.put("approved", contractService.countByInternConfirmStatus(InternConfirmStatus.APPROVED));
            stats.put("pending", contractService.countByInternConfirmStatus(InternConfirmStatus.PENDING));
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}