package com.example.Internship_System.hr.service;

import com.example.Internship_System.cloudinary.service.CloudinaryService;
import com.example.Internship_System.cloudinary.util.FileValidationUtil;
import com.example.Internship_System.hr.dto.HRContractDTO;
import com.example.Internship_System.intern.entity.ContractDocument;
import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import com.example.Internship_System.intern.entity.InternProfile;
import com.example.Internship_System.repository.ContractDocumentRepository;
import com.example.Internship_System.repository.InternRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HRContractService {

    @Autowired
    private ContractDocumentRepository contractRepository;

    @Autowired
    private InternRepository internRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Get all contracts with intern info for HR view with pagination, search and filter
     */
    public Page<HRContractDTO> getContractsForHR(String searchTerm, String status, Pageable pageable) {
        List<HRContractDTO> allContracts = new ArrayList<>();
        
        // Handle NOT_UPLOAD status - get interns without contracts
        if (status != null && status.equals("NOT_UPLOAD")) {
            List<Object[]> internsWithoutContracts = contractRepository.findInternsWithoutContracts(
                searchTerm != null && !searchTerm.trim().isEmpty() ? searchTerm : null);
            
            for (Object[] result : internsWithoutContracts) {
                InternProfile intern = (InternProfile) result[0];
                User user = (User) result[1];
                
                HRContractDTO dto = new HRContractDTO();
                dto.setInternId(intern.getInternId());
                dto.setFullName(user.getFullName());
                dto.setPhone(user.getPhone());
                dto.setContractStatus(ContractStatus.NOT_UPLOAD);
                dto.setFilePath(null);
                dto.setDocumentId(null);
                dto.setInternConfirmStatus(null);
                dto.setConfirmAt(null);
                dto.setCreatedAt(null);
                dto.setNote(null);
                
                allContracts.add(dto);
            }
        } else {
            // Get contracts with status filter
            List<Object[]> contracts = contractRepository.findContractsForHR(
                searchTerm != null && !searchTerm.trim().isEmpty() ? searchTerm : null,
                status != null && !status.isEmpty() ? status : null);
            
            for (Object[] result : contracts) {
                ContractDocument contract = (ContractDocument) result[0];
                InternProfile intern = (InternProfile) result[1];
                User user = (User) result[2];
                
                HRContractDTO dto = new HRContractDTO();
                dto.setDocumentId(contract.getDocumentId());
                dto.setInternId(intern.getInternId());
                dto.setFullName(user.getFullName());
                dto.setPhone(user.getPhone());
                dto.setFilePath(contract.getFilePath());
                dto.setContractStatus(contract.getContractStatus());
                dto.setInternConfirmStatus(contract.getInternConfirmStatus());
                dto.setConfirmAt(contract.getConfirmAt());
                dto.setNote(contract.getNote());
                // Using confirmAt as createdAt fallback
                dto.setCreatedAt(contract.getConfirmAt());
                
                allContracts.add(dto);
            }
        }
        
        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allContracts.size());
        List<HRContractDTO> pageContent = start < allContracts.size() ? 
            allContracts.subList(start, end) : new ArrayList<>();
        
        return new PageImpl<>(pageContent, pageable, allContracts.size());
    }

    /**
     * Upload contract for an intern
     */
    @Transactional
    public HRContractDTO uploadContract(Integer internId, MultipartFile file, String note) throws IOException {
        // Validate file
        FileValidationUtil.FileValidationResult validationResult = 
            FileValidationUtil.validateCvFile(file); // Use CV validation (PDF, DOC, DOCX)
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(validationResult.getMessage());
        }

        // Get intern
        InternProfile intern = internRepository.findById(internId)
            .orElseThrow(() -> new RuntimeException("Intern not found with id: " + internId));

        // Check if contract already exists
        Optional<ContractDocument> existingContractOpt = contractRepository.findByIntern(intern);
        
        // Upload file to Cloudinary
        Map<String, Object> uploadResult = cloudinaryService.upload(file, "contracts");
        String fileUrl = (String) uploadResult.get("url");

        ContractDocument contract;
        if (existingContractOpt.isPresent()) {
            // Update existing contract
            contract = existingContractOpt.get();
            // Delete old file from Cloudinary if exists
            if (contract.getFilePath() != null && !contract.getFilePath().isEmpty()) {
                try {
                    String oldPublicId = extractPublicIdFromUrl(contract.getFilePath());
                    if (oldPublicId != null) {
                        cloudinaryService.delete(oldPublicId);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to delete old contract file: " + e.getMessage());
                }
            }
            contract.setFilePath(fileUrl);
            contract.setContractStatus(ContractStatus.UPLOAD);
            contract.setInternConfirmStatus(InternConfirmStatus.PENDING);
            contract.setConfirmAt(LocalDateTime.now());
        } else {
            // Create new contract
            contract = new ContractDocument();
            contract.setIntern(intern);
            contract.setFilePath(fileUrl);
            contract.setContractStatus(ContractStatus.UPLOAD);
            contract.setInternConfirmStatus(InternConfirmStatus.PENDING);
            contract.setConfirmAt(LocalDateTime.now());
        }

        if (note != null && !note.trim().isEmpty()) {
            contract.setNote(note);
        }

        contract = contractRepository.save(contract);
        return contractToDTO(contract);
    }

    /**
     * Replace existing contract
     */
    @Transactional
    public HRContractDTO replaceContract(Integer documentId, MultipartFile file, String note) throws IOException {
        // Validate file
        FileValidationUtil.FileValidationResult validationResult = 
            FileValidationUtil.validateCvFile(file);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(validationResult.getMessage());
        }

        // Get contract
        ContractDocument contract = contractRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + documentId));

        // Delete old file from Cloudinary
        if (contract.getFilePath() != null && !contract.getFilePath().isEmpty()) {
            try {
                String oldPublicId = extractPublicIdFromUrl(contract.getFilePath());
                if (oldPublicId != null) {
                    cloudinaryService.delete(oldPublicId);
                }
            } catch (Exception e) {
                System.err.println("Failed to delete old contract file: " + e.getMessage());
            }
        }

        // Upload new file
        Map<String, Object> uploadResult = cloudinaryService.upload(file, "contracts");
        String fileUrl = (String) uploadResult.get("url");

        contract.setFilePath(fileUrl);
        contract.setContractStatus(ContractStatus.UPLOAD);
        contract.setInternConfirmStatus(InternConfirmStatus.PENDING);
        contract.setConfirmAt(LocalDateTime.now());

        if (note != null && !note.trim().isEmpty()) {
            contract.setNote(note);
        }

        contract = contractRepository.save(contract);
        return contractToDTO(contract);
    }

    /**
     * Delete contract
     */
    @Transactional
    public void deleteContract(Integer documentId) {
        ContractDocument contract = contractRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + documentId));

        // Delete file from Cloudinary
        if (contract.getFilePath() != null && !contract.getFilePath().isEmpty()) {
            try {
                String publicId = extractPublicIdFromUrl(contract.getFilePath());
                if (publicId != null) {
                    cloudinaryService.delete(publicId);
                }
            } catch (Exception e) {
                System.err.println("Failed to delete contract file from Cloudinary: " + e.getMessage());
            }
        }

        contractRepository.deleteById(documentId);
    }

    /**
     * Update contract note
     */
    @Transactional
    public HRContractDTO updateContractNote(Integer documentId, String note) {
        ContractDocument contract = contractRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + documentId));

        contract.setNote(note);
        contract = contractRepository.save(contract);
        return contractToDTO(contract);
    }

    /**
     * Get contract file URL for download
     */
    public String getContractFileUrl(Integer documentId) {
        ContractDocument contract = contractRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + documentId));
        return contract.getFilePath();
    }

    // Helper methods
    private HRContractDTO contractToDTO(ContractDocument contract) {
        InternProfile intern = contract.getIntern();
        if (intern == null) {
            return new HRContractDTO(
                contract.getDocumentId(),
                null,
                null,
                null,
                contract.getFilePath(),
                contract.getContractStatus(),
                contract.getInternConfirmStatus(),
                contract.getConfirmAt(),
                contract.getConfirmAt(),
                contract.getNote()
            );
        }

        // Get user info
        User user = userRepository.findById(intern.getUserId()).orElse(null);

        return new HRContractDTO(
            contract.getDocumentId(),
            intern.getInternId(),
            user != null ? user.getFullName() : null,
            user != null ? user.getPhone() : null,
            contract.getFilePath(),
            contract.getContractStatus(),
            contract.getInternConfirmStatus(),
            contract.getConfirmAt(),
            contract.getConfirmAt(),
            contract.getNote()
        );
    }

    private String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex != -1) {
                String afterUpload = url.substring(uploadIndex + 8);
                if (afterUpload.matches("^v\\d+/.*")) {
                    afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
                }
                int lastDot = afterUpload.lastIndexOf('.');
                if (lastDot != -1) {
                    afterUpload = afterUpload.substring(0, lastDot);
                }
                return afterUpload;
            }
        } catch (Exception e) {
            System.err.println("Failed to extract public ID from URL: " + url);
        }
        return null;
    }
}

