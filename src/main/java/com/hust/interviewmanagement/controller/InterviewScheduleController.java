package com.hust.interviewmanagement.controller;

import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.enums.EResult;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.service.CandidateService;
import com.hust.interviewmanagement.service.InterviewService;
import com.hust.interviewmanagement.service.SkillService;
import com.hust.interviewmanagement.service.UserService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.utils.SearchRequestUtil;
import com.hust.interviewmanagement.utils.SessionUtil;
import com.hust.interviewmanagement.web.request.InterviewRequest;
import com.hust.interviewmanagement.web.request.ResultRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewScheduleController {
    private final SkillService skillService;
    private final UserService userService;
    private final CandidateService candidateService;
    private final SessionUtil sessionUtil;
    private final InterviewService interviewService;
    private final SearchRequestUtil searchRequestUtil;

    @GetMapping({"", "/"})
    public String getJobs(HttpServletRequest req,
                          Model model,
                          @ModelAttribute SearchRequest searchRequest) {
        sessionUtil.setTitle(req, "INTERVIEW SCHEDULE");
        Page<InterviewSchedule> interviewSchedules = interviewService
                .findAllInterviewSchedule(searchRequestUtil.getSearchRequest(searchRequest));
        List<Users> users = userService.findUserByRoleInterview();
        SearchRequest searchResponse = searchRequestUtil
                .setPageMax(interviewSchedules.getTotalPages(), searchRequest);
        model.addAttribute(MessageList.Common.INTERVIEWS, users);
        model.addAttribute(MessageList.Common.SEARCH_RESPONSE, searchResponse);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULES, interviewSchedules);
        return "ui/interview/list";
    }

    @GetMapping("/create")
    public String getCreateInterview(@ModelAttribute InterviewRequest interviewRequest,
                                     Model model) {
        model.addAttribute(MessageList.Common.SKILLS, skillService.findAllSkill());
        model.addAttribute(MessageList.Common.CANDIDATES, candidateService.findCandidateByStatus(EStatus.OPEN));
        model.addAttribute(MessageList.Common.USERS, userService.findUserByRoleInterviewAndRecruiter());
        return "ui/interview/add";
    }

    @PostMapping("/create")
    public String postCreateInterview(@ModelAttribute InterviewRequest interviewRequest,
                                      RedirectAttributes ra) throws MessagingException {
        interviewService.saveInterviewSchedule(interviewRequest);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageInterview.CREATE_INTERVIEW_SUCCESS);
        return "redirect:/interview/create";
    }

    @GetMapping("/{id}")
    public String getInterview(@PathVariable Long id, Model model) {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULE, interviewSchedule);
        return "ui/interview/detail";
    }

    @GetMapping("/result/{id}")
    public String getResult(@PathVariable Long id,
                            Model model) {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULE, interviewSchedule);
        return "ui/interview/result";
    }

    @PostMapping("/result/{id}")
    public String setResult(@PathVariable Long id,
                            Model model,
                            ResultRequest resultRequest) {

        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id);
        EStatus status = this.getStatus(resultRequest.getResult());
        interviewSchedule.setStatus(status);
        interviewSchedule.getResultInterviews().setResult(resultRequest.getResult());
        interviewSchedule.getResultInterviews().setNote(resultRequest.getNote());
        interviewSchedule.getResultInterviews().getCandidate().setStatus(status);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULE, interviewService.saveInterviewSchedule(interviewSchedule));
        model.addAttribute(MessageList.Common.ALERT, MessageList.MessageInterview.EDIT_INTERVIEW_SUCCESS);
        return "redirect:/interview/" + id;
    }

    @GetMapping("/edit/{id}")
    public String getEditInterview(@PathVariable Long id,
                                   @ModelAttribute InterviewRequest interviewRequest,
                                   Model model) {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id);
        List<Users> users = userService.findUserByRoleInterviewAndRecruiter();
        List<Long> ids = interviewSchedule.getInterviewer().stream().map(x -> x.getInterviewer().getId()).toList();
        model.addAttribute(MessageList.Common.USERS, users);
        model.addAttribute(MessageList.Common.INTERVIEWERS_ID, ids);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULE, interviewSchedule);
        return "ui/interview/edit";
    }

    @PostMapping("/edit/{id}")
    public String editInterview(@PathVariable Long id,
                                Model model,
                                @ModelAttribute InterviewRequest interviewRequest
    ) throws MessagingException {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id);
        EResult result = interviewSchedule.getResultInterviews().getResult();
        if (result != EResult.PASS && result != EResult.FAIL) {
            interviewService.updateInterviewSchedule(interviewSchedule, interviewRequest);
            model.addAttribute(MessageList.Common.ALERT, MessageList.MessageInterview.EDIT_INTERVIEW_SUCCESS);
        } else {
            model.addAttribute(MessageList.Common.ALERT, MessageList.MessageInterview.EDIT_INTERVIEW_FAil);
        }
        return "redirect:/interview/edit/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteInterview(@PathVariable Long id) throws MessagingException {
        interviewService.deleteInterviewScheduleById(id);
        return "redirect:/interview";
    }


    private EStatus getStatus(EResult result) {
        if (result == EResult.FAIL) {
            return EStatus.FAILED_INTERVIEW;
        }
        if (result == EResult.CANCEL) {
            return EStatus.CANCELED_INTERVIEW;
        }
        return EStatus.PASSED_INTERVIEW;
    }
}
