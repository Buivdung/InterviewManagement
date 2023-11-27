package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.web.request.JobRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.result.JobSave;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobService {

    JobSave saveJob(JobRequest jobRequest);

    Page<Job> findAllJob(SearchRequest searchRequest);

    void deleteJobById(Long id);

    Job findJobById(Long id);

    Job updateJob(Job job, JobRequest jobRequest);

    List<Job> saveAllJobs(MultipartFile fileImport);
}
