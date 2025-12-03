package com.example.Internship_System.intern.service;

import com.example.Internship_System.intern.entity.ContractDocument;
import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.repository.ContractDocumentRepository;
import com.example.Internship_System.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContractDocumentService {

    @Autowired
    private ContractDocumentRepository contractRepository;

    @Autowired
    private InternRepository internRepository;

    /**
     * Save or update a contract document
     */
    @Transactional
    public ContractDocument save(ContractDocument contract) {
        return contractRepository.save(contract);
    }

    /**
     * Find all contract documents
     */
    public List<ContractDocument> findAll() {
        return contractRepository.findAll();
    }

    /**
     * Find contract document by ID
     */
    public Optional<ContractDocument> findById(int id) {
        return contractRepository.findById(id);
    }

    /**
     * Find contract document by intern ID
     */
    public Optional<ContractDocument> findByInternId(int internId) {
        Optional<InternProfile> internOpt = internRepository.findById(internId);
        if (internOpt.isPresent()) {
            return contractRepository.findByIntern(internOpt.get());
        }
        return Optional.empty();
    }

    /**
     * Find contract documents by contract status
     */
    public List<ContractDocument> findByContractStatus(ContractStatus status) {
        return contractRepository.findByContractStatus(status);
    }

    /**
     * Find contract documents by intern confirm status
     */
    public List<ContractDocument> findByInternConfirmStatus(InternConfirmStatus status) {
        return contractRepository.findByInternConfirmStatus(status);
    }

    /**
     * Update contract status
     */
    @Transactional
    public ContractDocument updateContractStatus(int id, ContractStatus status) {
        ContractDocument contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        
        contract.setContractStatus(status);
        return contractRepository.save(contract);
    }

    /**
     * Update intern confirm status with optional note
     */
    @Transactional
    public ContractDocument updateInternConfirmStatus(int id, InternConfirmStatus status, String note) {
        ContractDocument contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        
        contract.setInternConfirmStatus(status);
        contract.setConfirmAt(LocalDateTime.now());
        
        if (note != null && !note.trim().isEmpty()) {
            contract.setNote(note);
        }
        
        return contractRepository.save(contract);
    }

    /**
     * Create contract for intern
     */
    @Transactional
    public ContractDocument createContractForIntern(int internId, String filePath) {
        InternProfile intern = internRepository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern not found with id: " + internId));
        
        // Check if contract already exists
        Optional<ContractDocument> existingContract = contractRepository.findByIntern(intern);
        if (existingContract.isPresent()) {
            throw new RuntimeException("Contract already exists for this intern");
        }
        
        ContractDocument contract = new ContractDocument();
        contract.setIntern(intern);
        contract.setFilePath(filePath);
        contract.setContractStatus(ContractStatus.UPLOAD);
        contract.setInternConfirmStatus(InternConfirmStatus.PENDING);
        contract.setConfirmAt(LocalDateTime.now());
        
        return contractRepository.save(contract);
    }

    /**
     * Update contract file path
     */
    @Transactional
    public ContractDocument updateContractFile(int id, String filePath) {
        ContractDocument contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        
        contract.setFilePath(filePath);
        contract.setContractStatus(ContractStatus.UPLOAD);
        
        return contractRepository.save(contract);
    }

    /**
     * Approve contract
     */
    @Transactional
    public ContractDocument approveContract(int id, String note) {
        ContractDocument contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        
        contract.setInternConfirmStatus(InternConfirmStatus.APPROVED);
        contract.setConfirmAt(LocalDateTime.now());
        
        if (note != null && !note.trim().isEmpty()) {
            contract.setNote(note);
        }
        
        return contractRepository.save(contract);
    }

    /**
     * Delete contract by ID
     */
    @Transactional
    public void deleteById(int id) {
        contractRepository.deleteById(id);
    }

    /**
     * Count all contracts
     */
    public long countAll() {
        return contractRepository.count();
    }

    /**
     * Count contracts by contract status
     */
    public long countByContractStatus(ContractStatus status) {
        return contractRepository.countByContractStatus(status);
    }

    /**
     * Count contracts by intern confirm status
     */
    public long countByInternConfirmStatus(InternConfirmStatus status) {
        return contractRepository.countByInternConfirmStatus(status);
    }

    /**
     * Check if intern has contract
     */
    @SuppressWarnings("unused")
    public boolean hasContract(int internId) {
        Optional<InternProfile> internOpt = internRepository.findById(internId);
        return internOpt.filter(internProfile -> contractRepository.findByIntern(internProfile).isPresent()).isPresent();
    }

    /**
     * Get pending contracts (not uploaded or pending approval)
     */
    @SuppressWarnings("unused")
    public List<ContractDocument> getPendingContracts() {
        return contractRepository.findByInternConfirmStatus(InternConfirmStatus.PENDING);
    }

    /**
     * Get approved contracts
     */
    @SuppressWarnings("unused")
    public List<ContractDocument> getApprovedContracts() {
        return contractRepository.findByInternConfirmStatus(InternConfirmStatus.APPROVED);
    }
}