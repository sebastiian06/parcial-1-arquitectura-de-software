package com.iglesia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    
    // Para DashboardController - Query corregida (accede a church a través de person)
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Offering o WHERE o.person.church.id = :churchId AND o.status = :status")
    Double sumAmountByChurchIdAndStatus(@Param("churchId") Long churchId, @Param("status") String status);
    
    // Para OfferingController
    List<Offering> findAllByPersonChurchId(Long churchId);
}