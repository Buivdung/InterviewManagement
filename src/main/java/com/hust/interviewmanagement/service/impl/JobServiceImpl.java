package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.*;
import com.hust.interviewmanagement.service.FileService;
import com.hust.interviewmanagement.service.JobService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.web.request.JobImport;
import com.hust.interviewmanagement.web.request.JobRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.result.JobSave;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    private final SkillRepository skillRepository;
    private final BenefitsRepository benefitsRepository;
    private final LevelRepository levelRepository;
    private final SkillJobRepository skillJobRepository;
    private final BenefitsJobRepository benefitsJobRepository;
    private final FileService<JobImport> fileService;

    @Override
    @Transactional
    public JobSave saveJob(JobRequest jobRequest) {
        if (validateDate(jobRequest)) {
            return JobSave.builder().job(null).message(MessageList.MessageJob.M_DATE).build();
        } else if (validateSalary(jobRequest)) {
            return JobSave.builder().job(null).message(MessageList.MessageJob.M_SALARY).build();
        } else {
            Job job = mapJob(new Job(), jobRequest);
            if (jobRepository.existsJobByTitleAndLevelAndStatus(job.getTitle(), job.getLevel(), EStatus.OPEN)) {
                return JobSave.builder().job(null).message(MessageList.MessageJob.DUPLICATE_JOB).build();
            }
            return JobSave.builder().job(jobRepository.save(job)).build();
        }
    }

    @Override
    public Page<Job> findAllJob(SearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber() - 1, SearchRequest.PAGE_SIZE);
        if (searchRequest.getStatus() != null) {
            return jobRepository.findAllJob(searchRequest.getParam(), searchRequest.getStatus(), pageable);
        }
        return jobRepository.findAllJob(searchRequest.getParam(), pageable);
    }

    @Override
    public void deleteJobById(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Job findJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageJob.DONT_JOB));
    }

    @Override
    @Transactional
    public Job updateJob(Job job, JobRequest jobRequest) {
        if (jobRepository.existsJobByTitleAndLevel_IdAndStatusAndIdNot(
                jobRequest.getTitle(),
                jobRequest.getLevel(),
                EStatus.OPEN,
                jobRequest.getId())) {
            return null;
        }
        benefitsJobRepository.deleteBenefitsJobBy(jobRequest.getId());
        skillJobRepository.deleteSkillJob(jobRequest.getId());
        Job jobMap = mapJob(job, jobRequest);
        return jobRepository.save(jobMap);
    }

    @Override
    @Transactional
    public List<Job> saveAllJobs(MultipartFile file) {
        List<Job> jobs = getJobs(file);
        for (Job j : jobs) {
            if (jobRepository.existsJobByTitleAndLevelAndStatus(j.getTitle(), j.getLevel(), EStatus.OPEN)) {
                return Collections.emptyList();
            }
        }
        return jobRepository.saveAll(jobs);
    }

    private Job mapJob(Job job, JobRequest jobRequest) {
        modelMapper.map(jobRequest, job);
        Level level = levelRepository.findById(jobRequest.getLevel()).orElseThrow();
        List<Skill> skills = skillRepository.findAllById(jobRequest.getSkills());
        List<Benefits> benefits = benefitsRepository.findAllById(jobRequest.getBenefits());
        List<SkillJob> skillJobs = getSkillJobs(skills, job);
        List<BenefitsJob> benefitsJobs = getBenefitJobs(benefits, job);
        job.setLevel(level);
        job.setStatus(EStatus.OPEN);
        job.setSkillJobs(skillJobs);
        job.setBenefitsJobs(benefitsJobs);
        return job;
    }

    private List<Job> getJobs(MultipartFile file) {
        List<Job> jobs = new ArrayList<>();
        List<JobImport> jobImports = fileService.importExcel(file, JobImport.class);
        for (JobImport j : jobImports) {
            Job job = modelMapper.map(j, Job.class);
            Level level = levelRepository.findAllByName(j.getLevel()).orElseThrow();
            List<Skill> skills = getSkills(j.getSkills());
            List<Benefits> benefits = getBenefits(j.getBenefits());
            List<SkillJob> skillJobs = getSkillJobs(skills, job);
            List<BenefitsJob> benefitsJobs = getBenefitJobs(benefits, job);
            job.setStatus(EStatus.OPEN);
            job.setLevel(level);
            job.setSkillJobs(skillJobs);
            job.setBenefitsJobs(benefitsJobs);
            jobs.add(job);
        }
        return jobs;
    }

    private List<Benefits> getBenefits(String benefitName) {
        String[] arr = benefitName.split(",");
        List<String> benefitNames = Arrays.stream(arr).map(String::trim).toList();
        return benefitsRepository.findAllByNameIn(benefitNames);
    }

    private List<Skill> getSkills(String skillName) {
        String[] arr = skillName.split(",");
        List<String> skillNames = Arrays.stream(arr).map(String::trim).toList();
        return skillRepository.findAllByNameIn(skillNames);
    }

    private List<BenefitsJob> getBenefitJobs(List<Benefits> benefits, Job job) {
        List<BenefitsJob> benefitsJobs = new ArrayList<>();
        for (Benefits b : benefits) {
            benefitsJobs.add(BenefitsJob
                    .builder()
                    .job(job)
                    .benefits(b)
                    .build()
            );
        }
        return benefitsJobs;
    }

    private List<SkillJob> getSkillJobs(List<Skill> skills, Job job) {

        List<SkillJob> skillJobs = new ArrayList<>();
        for (Skill s : skills) {
            skillJobs.add(SkillJob
                    .builder()
                    .skill(s)
                    .job(job)
                    .build()
            );
        }
        return skillJobs;
    }

    private boolean validateDate(JobRequest jobRequest) {
        return jobRequest.getEndDate().isBefore(jobRequest.getStartDate());
    }

    private boolean validateSalary(JobRequest jobRequest) {
        return jobRequest.getSalaryFrom().compareTo(jobRequest.getSalaryTo()) > 0;
    }
}
