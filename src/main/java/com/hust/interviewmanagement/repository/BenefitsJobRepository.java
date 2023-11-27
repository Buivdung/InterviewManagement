package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.BenefitsJob;
import com.hust.interviewmanagement.entities.KeyBenefitsJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BenefitsJobRepository extends JpaRepository<BenefitsJob, KeyBenefitsJob> {

    @Modifying
    @Query("DELETE BenefitsJob WHERE job.id = ?1")
    void deleteBenefitsJobBy(Long id);
}
