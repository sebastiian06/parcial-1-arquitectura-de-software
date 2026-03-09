package com.iglesia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Para PaymentController
    List<Payment> findAllByStatus(PaymentStatus status);
    
    // Para DashboardController - Query corregida
    @Query("SELECT COUNT(p) FROM Payment p WHERE " +
           "((p.type = 'OFFERING' AND p.referenceId IN (SELECT o.id FROM Offering o WHERE o.person.church.id = :churchId)) " +
           "OR (p.type = 'ENROLLMENT' AND p.referenceId IN (SELECT e.id FROM Enrollment e WHERE e.person.church.id = :churchId))) " +
           "AND p.status = :status")
    long countByChurchIdAndStatus(@Param("churchId") Long churchId, @Param("status") String status);
}