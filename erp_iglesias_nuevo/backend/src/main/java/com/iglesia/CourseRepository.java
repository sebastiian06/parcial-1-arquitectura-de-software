package com.iglesia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByChurchId(Long churchId);
    long countByChurchIdAndActiveTrue(Long churchId);
    List<Course> findAllByChurchId(Long churchId);  // Para CourseController
}