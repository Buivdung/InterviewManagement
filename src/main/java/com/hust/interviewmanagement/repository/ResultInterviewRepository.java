package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.ResultInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResultInterviewRepository extends JpaRepository<ResultInterview, Long> {

    Optional<ResultInterview> findByCandidate_Id(Long id);
    Optional<ResultInterview> findByInterviewSchedule_Id(Long id);
    @Modifying
    @Query("DELETE ResultInterview where interviewSchedule.id = ?1")
    void deleteByInterviewSchedule_Id(Long id);
}
