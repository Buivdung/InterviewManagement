package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.entities.ResultInterview;
import com.hust.interviewmanagement.entities.Skill;
import com.hust.interviewmanagement.enums.EGender;
import com.hust.interviewmanagement.enums.EHighestLevel;
import com.hust.interviewmanagement.enums.EPosition;
import com.hust.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {


    @Query("select c from Candidate c where " +
            "c.fullName LIKE %?1% " +
            " AND c.status = ?2")
    Page<Candidate> findAll(String nameKeyword, EStatus status, Pageable pageable);

    @Query("select c from Candidate c where " +
            "c.fullName LIKE %?1%")
    Page<Candidate> findAll(String nameKeyword, Pageable pageable);

    List<Candidate> findAllByStatus(EStatus status);

}
