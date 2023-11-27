package com.hust.interviewmanagement.controller;

import com.hust.interviewmanagement.entities.Job;
import com.hust.interviewmanagement.service.BenefitsService;
import com.hust.interviewmanagement.service.JobService;
import com.hust.interviewmanagement.service.LevelService;
import com.hust.interviewmanagement.service.SkillService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.utils.SearchRequestUtil;
import com.hust.interviewmanagement.utils.SessionUtil;
import com.hust.interviewmanagement.web.request.JobRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.result.JobSave;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {
    private final SessionUtil sessionUtil;
    private final SearchRequestUtil searchRequestUtil;
    private final JobService jobService;
    private final BenefitsService benefitsService;
    private final SkillService skillService;
    private final LevelService levelService;

    @GetMapping({"", "/"})
    public String getJobs(HttpServletRequest req,
                          Model model,
                          @ModelAttribute SearchRequest searchRequest) {
        sessionUtil.setTitle(req, "JOB");
        Page<Job> jobs = jobService.findAllJob(searchRequestUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchRequestUtil.setPageMax(jobs.getTotalPages(), searchRequest);
        model.addAttribute(MessageList.Common.JOBS, jobs);
        model.addAttribute(MessageList.Common.SEARCH_RESPONSE, searchResponse);
        return "ui/job/list";
    }

    @GetMapping("/create")
    public String getCreateJob(Model model,
                               @ModelAttribute JobRequest jobRequest) {
        model.addAttribute(MessageList.Common.BENEFITS, benefitsService.findAllBenefits());
        model.addAttribute(MessageList.Common.SKILLS, skillService.findAllSkill());
        model.addAttribute(MessageList.Common.LEVELS, levelService.findAllLevel());
        return "ui/job/add";
    }

    @PostMapping("/create")
    public String jobCreateForm(@ModelAttribute JobRequest jobRequest,
                                RedirectAttributes ra) {
        JobSave jobSave = jobService.saveJob(jobRequest);
        if (jobSave.getJob() == null) {
            ra.addFlashAttribute(MessageList.Common.MESSAGE, jobSave.getMessage());
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageJob.CREATE_JOB_FAIL);
            ra.addFlashAttribute("jobRequest", jobRequest);
        } else {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageJob.CREATE_JOB_SUCCESS);
        }
        return "redirect:/job/create";
    }

    @GetMapping("/{id}")
    public String getJob(@PathVariable Long id,
                         Model model) {
        Job job = jobService.findJobById(id);
        model.addAttribute(MessageList.Common.JOB, job);
        return "ui/job/detail";
    }

    @GetMapping("/edit/{id}")
    public String getEditJob(@PathVariable Long id,
                             Model model,
                             @ModelAttribute JobRequest jobRequest) {

        Job job = jobService.findJobById(id);
        List<Long> skillIds = job.getSkillJobs().stream().map(s -> s.getSkill().getId()).toList();
        List<Long> benefitsIds = job.getBenefitsJobs().stream().map(s -> s.getBenefits().getId()).toList();
        model.addAttribute(MessageList.Common.JOB, job);
        model.addAttribute(MessageList.Common.BENEFITS, benefitsService.findAllBenefits());
        model.addAttribute(MessageList.Common.SKILLS, skillService.findAllSkill());
        model.addAttribute(MessageList.Common.LEVELS, levelService.findAllLevel());
        model.addAttribute(MessageList.Common.SKILLIDS, skillIds);
        model.addAttribute(MessageList.Common.BENEFITIDS, benefitsIds);
        return "ui/job/edit";
    }

    @PostMapping("/edit/{id}")
    public String postEditJob(@PathVariable Long id,
                              Model model,
                              @ModelAttribute JobRequest jobRequest) {
        if (validateDate(jobRequest)) {
            model.addAttribute(MessageList.MessageJob.VALIDATE_DATE, MessageList.MessageJob.M_DATE);
        } else if (validateSalary(jobRequest)) {
            model.addAttribute(MessageList.MessageJob.VALIDATE_SALARY, MessageList.MessageJob.M_SALARY);
        } else if (!Objects.equals(id, jobRequest.getId())) {
            model.addAttribute(MessageList.Common.ALERT, MessageList.MessageJob.EDIT_JOB_FAIL);
        } else {
            Job job = jobService.findJobById(id);
            if (jobService.updateJob(job, jobRequest) != null) {
                model.addAttribute(MessageList.Common.ALERT, MessageList.MessageJob.EDIT_JOB_SUCCESS);
            } else {
                model.addAttribute(MessageList.Common.ALERT, MessageList.MessageJob.CREATE_JOB_FAIL);
                model.addAttribute(MessageList.Common.MESSAGE, MessageList.MessageJob.DUPLICATE_JOB);
            }
        }
        return getEditJob(id, model, jobRequest);
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id, RedirectAttributes ra) {
        jobService.deleteJobById(id);
        ra.addFlashAttribute(MessageList.Common.MESSAGE, MessageList.MessageJob.DELETE_JOB_SUCCESS);
        return "redirect:/job";
    }

    @PostMapping("/import")
    public String importJob(@ModelAttribute MultipartFile fileImport,
                            RedirectAttributes ra) {
        if (jobService.saveAllJobs(fileImport).isEmpty()) {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageJob.CREATE_JOB_FAIL);
            ra.addFlashAttribute(MessageList.Common.MESSAGE, MessageList.MessageJob.DUPLICATE_JOB);
        } else {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageJob.CREATE_JOB_SUCCESS);
        }
        return "redirect:/job";
    }

    private boolean validateDate(JobRequest jobRequest) {
        return jobRequest.getEndDate().isBefore(jobRequest.getStartDate());
    }

    private boolean validateSalary(JobRequest jobRequest) {
        return jobRequest.getSalaryFrom().compareTo(jobRequest.getSalaryTo()) > 0;
    }
}
