package com.hust.interviewmanagement.controller;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.service.*;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.utils.SearchRequestUtil;
import com.hust.interviewmanagement.utils.SessionUtil;
import com.hust.interviewmanagement.web.request.OfferRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.resp.CandidateResp;
import com.hust.interviewmanagement.web.resp.OfferExport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
    private final CandidateService candidateService;
    private final UserService userService;
    private final LevelService levelService;
    private final DepartmentService departmentService;
    private final FileService<OfferExport> fileService;
    private final ResultInterviewService resultInterviewService;
    private final OfferService offerService;
    private final SessionUtil sessionUtil;
    private final SearchRequestUtil searchRequestUtil;

    @GetMapping({"", "/"})
    public String getJobs(HttpServletRequest req,
                          Model model,
                          @ModelAttribute SearchRequest searchRequest) {
        sessionUtil.setTitle(req, "OFFER");
        Page<Offer> offers = offerService.findAllOffer(searchRequestUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchRequestUtil.setPageMax(offers.getTotalPages(), searchRequest);
        model.addAttribute(MessageList.Common.OFFERS, offers);
        model.addAttribute(MessageList.Common.DEPARTMENTS, departmentService.findAllDepartment());
        model.addAttribute(MessageList.Common.SEARCH_RESPONSE, searchResponse);
        return "ui/offer/list";
    }

    @GetMapping("/create")
    public String getCreateOffer(Model model) {
        List<Candidate> candidates = candidateService.findCandidateByStatus(EStatus.PASSED_INTERVIEW);
        List<Users> users = userService.findUserByRoleRecruiterAndManager();
        List<Level> levels = levelService.findAllLevel();
        List<Department> departments = departmentService.findAllDepartment();
        model.addAttribute(MessageList.Common.USERS, users);
        model.addAttribute(MessageList.Common.DEPARTMENTS, departments);
        model.addAttribute(MessageList.Common.LEVELS, levels);
        model.addAttribute(MessageList.Common.CANDIDATES, candidates);
        model.addAttribute(MessageList.Common.OFFER, new OfferRequest());
        return "ui/offer/add";
    }

    @PostMapping("/create")
    public String postCreateOffer(@ModelAttribute OfferRequest offerRequest, Model model, RedirectAttributes ra) {
        offerService.saveOffer(offerRequest);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageOffer.CREATE_OFFER_SUCCESS);
        return "redirect:/offer/create";
    }

    @GetMapping("/{id}")
    public String getOffer(@PathVariable Long id, Model model) {
        Offer offer = offerService.findOfferById(id);
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        model.addAttribute(MessageList.Common.OFFER, offer);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULES, interviewerSchedules);
        return "ui/offer/detail";
    }

    @GetMapping("/edit/{id}")
    public String getOfferEdit(@PathVariable Long id, Model model) {
        Offer offer = offerService.findOfferById(id);
        List<Users> users = userService.findUserByRoleRecruiterAndManager();
        List<Level> levels = levelService.findAllLevel();
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        List<Department> departments = departmentService.findAllDepartment();
        model.addAttribute(MessageList.Common.USERS, users);
        model.addAttribute(MessageList.Common.DEPARTMENTS, departments);
        model.addAttribute(MessageList.Common.LEVELS, levels);
        model.addAttribute(MessageList.Common.OFFER, offer);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULES, interviewerSchedules);
        return "ui/offer/edit";
    }

    @PostMapping("/edit/{id}")
    public String offerEdit(@PathVariable Long id,
                            RedirectAttributes ra,
                            OfferRequest offerRequest) {
        offerService.updateOffer(offerRequest);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageOffer.EDIT_OFFER_SUCCESS);
        return "redirect:/offer/edit/" + id;
    }

    @GetMapping("/approve/{id}")
    public String getApproveOffer(@PathVariable Long id, Model model) {
        Offer offer = offerService.findOfferById(id);
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        model.addAttribute(MessageList.Common.OFFER, offer);
        model.addAttribute(MessageList.Common.INTERVIEW_SCHEDULES, interviewerSchedules);
        return "ui/offer/approve";
    }

    @GetMapping("/approve/accepted/{id}")
    @ResponseBody
    public void approveOffer(@PathVariable Long id,@RequestParam(required = false) String notes) {
        offerService.approveOffer(id,notes);
    }

    @GetMapping("/approve/rejected/{id}")
    @ResponseBody
    public void rejectOffer(@PathVariable Long id,@RequestParam(required = false) String notes) {
        offerService.rejectOffer(id,notes);
    }

    @GetMapping("/getResultCandidate/{id}")
    @ResponseBody
    public CandidateResp getCandidateResp(@PathVariable Long id) {
        ResultInterview resultInterview = resultInterviewService.findResultInterviewByCandidate_Id(id);
        List<InterviewerSchedule> interviewerSchedules = resultInterview.getInterviewSchedule().getInterviewer();
        List<String> interview = interviewerSchedules.stream().map(x -> x.getInterviewer().getFullName()).toList();
        return CandidateResp.builder()
                .eStatus(resultInterview.getCandidate().getStatus())
                .notes(resultInterview.getNote())
                .interviews(interview)
                .build();
    }

    @GetMapping("/delete/{id}")
    public String deleteInterview(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return "redirect:/offer";
    }

    @PostMapping("/export")
    public void exportOffer(@RequestParam LocalDate fromDate,
                            @RequestParam LocalDate toDate,
                            HttpServletRequest req,
                            HttpServletResponse resp) throws IOException, ServletException {
        List<Offer> offers = offerService.findAllOfferByDate(fromDate, toDate);
        List<OfferExport> exports = offers.stream().map(this::getOfferExport).toList();
        if (!exports.isEmpty()) {
            fileService.export(resp, exports);
        } else {
            req.getRequestDispatcher("/offer").forward(req, resp);
        }
    }

    private OfferExport getOfferExport(Offer offer) {
        OfferExport offerExport = new OfferExport();
        offerExport.setApproved(offer.getManager().getFullName());
        offerExport.setCandidateName(offer.getResultInterview().getCandidate().getFullName());
        offerExport.setEmail(offer.getResultInterview().getCandidate().getEmail());
        offerExport.setInterviewNotes(offer.getResultInterview().getNote());
        offerExport.setDepartment(offer.getDepartment().getName());
        offerExport.setStatus(offer.getStatus().name());
        offerExport.setContractType(offer.getContractType().name());
        offerExport.setCreateDate(offer.getCreateDate().toString());
        offerExport.setUpdateDate(offer.getUpdateDate().toString());
        offerExport.setLevel(offer.getLevel().getName());
        offerExport.setDueDate(offer.getDueDate().toString());
        offerExport.setBasicSalary(offer.getBasicSalary().toString());
        offerExport.setNotes(offer.getNotes());
        return offerExport;
    }
}
