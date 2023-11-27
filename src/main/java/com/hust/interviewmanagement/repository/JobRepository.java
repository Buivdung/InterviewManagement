package com.hust.interviewmanagement.repository;


import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.entities.Level;
import com.hust.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("from Job j WHERE j.title like %?1% and j.status = ?2")
    Page<Job> findAllJob(String jobTitle, EStatus status, Pageable pageable);

    @Query("from Job j WHERE j.title like %?1%")
    Page<Job> findAllJob(String jobTitle, Pageable pageable);


    boolean existsJobByTitleAndLevelAndStatus(String title, Level level,EStatus status);
    boolean existsJobByTitleAndLevel_IdAndStatusAndIdNot(String title,Long levelId,EStatus status, Long id);
}
