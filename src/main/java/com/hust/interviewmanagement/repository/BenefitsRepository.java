package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BenefitsRepository extends JpaRepository<Benefits, Long> {
    List<Benefits> findAllByNameIn(List<String> names);
}
