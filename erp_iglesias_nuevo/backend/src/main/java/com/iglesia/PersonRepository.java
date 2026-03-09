package com.iglesia;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByChurchId(Long churchId);
    Optional<Person> findByDocument(String document);
    Optional<Person> findByEmail(String email);
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
    
    // ✅ Agregar este método que usa DashboardController
    long countByChurchId(Long churchId);
}