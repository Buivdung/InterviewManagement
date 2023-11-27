package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.KeySkillCandidate;
import com.hust.interviewmanagement.entities.SkillCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SkillCandidateRepository extends JpaRepository<SkillCandidate, KeySkillCandidate> {
    @Modifying
    @Query("DELETE SkillCandidate WHERE candidate.id = ?1")
    void deleteAllByCandidateId(Long id);
}
