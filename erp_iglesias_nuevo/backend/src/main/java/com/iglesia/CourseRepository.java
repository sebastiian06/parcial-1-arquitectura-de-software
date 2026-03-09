package com.iglesia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Para listar cursos por iglesia
    List<Course> findByChurchId(Long churchId);
    
    // Para DashboardController
    long countByChurchIdAndActiveTrue(Long churchId);
    
    // Para CourseController
    List<Course> findAllByChurchId(Long churchId);
}