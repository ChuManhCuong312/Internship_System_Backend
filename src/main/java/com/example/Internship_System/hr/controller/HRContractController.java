package com.example.Internship_System.hr.controller;

import com.example.Internship_System.hr.dto.HRContractDTO;
import com.example.Internship_System.hr.service.HRContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hr/contracts")
@CrossOrigin(origins = "*")
public class HRContractController {

    @Autowired
    private HRContractService hrContractService;

    /**
     * GET - Get all contracts with pagination, search and filter
     */
    @GetMapping
    public ResponseEntity<Page<HRContractDTO>> getContracts(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<HRContractDTO> contracts = hrContractService.getContractsForHR(searchTerm, status, pageable);
            return ResponseEntity.ok(contracts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST - Upload contract for an intern
     */
    @PostMapping(value = "/{internId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadContract(
            @PathVariable Integer internId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "note", required = false) String note) {
        try {
            HRContractDTO contract = hrContractService.uploadContract(internId, file, note);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contract uploaded successfully");
            response.put("contract", contract);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * PATCH - Replace existing contract
     */
    @PatchMapping(value = "/{documentId}/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> replaceContract(
            @PathVariable Integer documentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "note", required = false) String note) {
        try {
            HRContractDTO contract = hrContractService.replaceContract(documentId, file, note);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contract replaced successfully");
            response.put("contract", contract);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * DELETE - Delete contract
     */
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Map<String, Object>> deleteContract(@PathVariable Integer documentId) {
        try {
            hrContractService.deleteContract(documentId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contract deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET - Download contract file
     */
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadContract(@PathVariable Integer documentId) {
        try {
            String fileUrl = hrContractService.getContractFileUrl(documentId);
            
            if (fileUrl == null || fileUrl.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            // Download file from Cloudinary URL
            URI uri = URI.create(fileUrl);
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            byte[] fileBytes = connection.getInputStream().readAllBytes();
            
            // Extract filename from URL
            String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            // Remove query parameters if any
            if (filename.contains("?")) {
                filename = filename.substring(0, filename.indexOf("?"));
            }
            // If no filename, use default
            if (filename.isEmpty() || !filename.contains(".")) {
                filename = "contract_" + documentId + ".pdf";
            }
            
            ByteArrayResource resource = new ByteArrayResource(fileBytes);
            
            // Determine content type
            String contentType = "application/octet-stream";
            if (filename.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filename.toLowerCase().endsWith(".doc")) {
                contentType = "application/msword";
            } else if (filename.toLowerCase().endsWith(".docx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileBytes.length)
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PATCH - Update contract note
     */
    @PatchMapping("/{documentId}/note")
    public ResponseEntity<Map<String, Object>> updateContractNote(
            @PathVariable Integer documentId,
            @RequestBody Map<String, String> request) {
        try {
            String note = request.get("note");
            HRContractDTO contract = hrContractService.updateContractNote(documentId, note);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contract note updated successfully");
            response.put("contract", contract);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

