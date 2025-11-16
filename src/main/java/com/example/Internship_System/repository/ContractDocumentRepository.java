package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.ContractDocument;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractDocumentRepository extends JpaRepository<ContractDocument, Integer> {
    Optional<ContractDocument> findByIntern(InternProfile intern);
}
