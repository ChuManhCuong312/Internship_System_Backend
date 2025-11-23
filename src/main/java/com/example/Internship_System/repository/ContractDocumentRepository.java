package com.example.Internship_System.repository;

import com.example.Internship_System.intern.entity.ContractDocument;
import com.example.Internship_System.intern.entity.ContractStatus;
import com.example.Internship_System.intern.entity.InternConfirmStatus;
import com.example.Internship_System.intern.entity.InternProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractDocumentRepository extends JpaRepository<ContractDocument, Integer> {
    
    /**
     * Find contract by intern profile
     */
    Optional<ContractDocument> findByIntern(InternProfile intern);
    
    /**
     * Find contracts by contract status
     */
    List<ContractDocument> findByContractStatus(ContractStatus contractStatus);
    
    /**
     * Find contracts by intern confirm status
     */
    List<ContractDocument> findByInternConfirmStatus(InternConfirmStatus internConfirmStatus);
    
    /**
     * Find contracts by both contract status and intern confirm status
     */
    List<ContractDocument> findByContractStatusAndInternConfirmStatus(
            ContractStatus contractStatus, 
            InternConfirmStatus internConfirmStatus);
    
    /**
     * Count contracts by contract status
     */
    long countByContractStatus(ContractStatus contractStatus);
    
    /**
     * Count contracts by intern confirm status
     */
    long countByInternConfirmStatus(InternConfirmStatus internConfirmStatus);
    
    /**
     * Check if contract exists for intern
     */
    boolean existsByIntern(InternProfile intern);
    
    /**
     * Find all contracts with intern details ordered by confirm date
     */
    @Query("SELECT c FROM ContractDocument c " +
           "LEFT JOIN FETCH c.intern " +
           "ORDER BY c.confirmAt DESC")
    List<ContractDocument> findAllWithInternOrderByConfirmDate();
    
    /**
     * Find pending contracts (not uploaded or pending approval)
     */
    @Query("SELECT c FROM ContractDocument c " +
           "WHERE c.contractStatus = 'NOT_UPLOAD' " +
           "OR c.internConfirmStatus = 'PENDING' " +
           "ORDER BY c.confirmAt DESC")
    List<ContractDocument> findPendingContracts();
    
    /**
     * Find contracts by intern name or email
     */
    @Query("SELECT c FROM ContractDocument c " +
           "JOIN c.intern i " +
           "JOIN User u ON i.userId = u.userId " +
           "WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ContractDocument> searchContractsByIntern(@Param("searchTerm") String searchTerm);
    
    /**
     * Delete contract by intern
     */
    void deleteByIntern(InternProfile intern);
    
    /**
     * Find contracts with intern and user info for HR view with search and status filter
     */
    @Query("SELECT c, i, u FROM ContractDocument c " +
           "JOIN c.intern i " +
           "JOIN User u ON i.userId = u.userId " +
           "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
           "       LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "       u.phone LIKE CONCAT('%', :searchTerm, '%')) " +
           "AND (:status IS NULL OR :status = '' OR " +
           "     (:status = 'UPLOAD' AND c.contractStatus = 'UPLOAD') OR " +
           "     (:status = 'PENDING' AND c.internConfirmStatus = 'PENDING') OR " +
           "     (:status = 'APPROVED' AND c.internConfirmStatus = 'APPROVED')) " +
           "ORDER BY c.confirmAt DESC NULLS LAST")
    List<Object[]> findContractsForHR(@Param("searchTerm") String searchTerm, 
                                      @Param("status") String status);
    
    /**
     * Find interns without contracts with user info for HR view
     */
    @Query("SELECT i, u FROM InternProfile i " +
           "JOIN User u ON i.userId = u.userId " +
           "WHERE NOT EXISTS (SELECT 1 FROM ContractDocument c WHERE c.intern = i) " +
           "AND (:searchTerm IS NULL OR :searchTerm = '' OR " +
           "     LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "     u.phone LIKE CONCAT('%', :searchTerm, '%'))")
    List<Object[]> findInternsWithoutContracts(@Param("searchTerm") String searchTerm);
}