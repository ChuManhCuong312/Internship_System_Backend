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

    
}