package com.hust.interviewmanagement.controller;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.service.CandidateService;
import com.hust.interviewmanagement.service.FileService;
import com.hust.interviewmanagement.service.SkillService;
import com.hust.interviewmanagement.service.UserService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.utils.SearchRequestUtil;
import com.hust.interviewmanagement.utils.SessionUtil;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.JobRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
@MultipartConfig
public class CandidateController {
    private final SessionUtil sessionUtil;
    private final SkillService skillService;
    private final UserService userService;
    private final CandidateService candidateService;
    private final SearchRequestUtil searchRequestUtil;
    private final FileService<Object> fileService;

    @GetMapping({"", "/"})
    public String getJobs(HttpServletRequest req,
                          Model model,
                          @ModelAttribute SearchRequest searchRequest) {
        sessionUtil.setTitle(req, "CANDIDATE");
        Page<Candidate> candidates = candidateService.findAllCandidate(searchRequestUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchRequestUtil.setPageMax(candidates.getTotalPages(), searchRequest);
        model.addAttribute(MessageList.Common.CANDIDATES, candidates);
        model.addAttribute(MessageList.Common.SEARCH_RESPONSE, searchResponse);
        return "ui/candidate/list";
    }

    @GetMapping("/create")
    public String getCreateJob(Model model,
                               @ModelAttribute JobRequest jobRequest) {
        model.addAttribute(MessageList.Common.SKILLS, skillService.findAllSkill());
        model.addAttribute(MessageList.Common.RECRUITERS, userService.findUserByRoleRecruiter());
        model.addAttribute(MessageList.Common.CANDIDATES, new CandidateRequest());
        return "ui/candidate/add";
    }

    @PostMapping("/create")
    public String createCandidate(@ModelAttribute CandidateRequest candidateRequest,
                                  RedirectAttributes ra) throws IOException {
        candidateService.saveCandidate(candidateRequest);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageCandidate.CREATE_CANDIDATE_SUCCESS);
        ra.addFlashAttribute(MessageList.Common.CANDIDATES, candidateRequest);
        return "redirect:/candidate/create";
    }

    @GetMapping("/{id}")
    public String candidateDetail(@PathVariable Long id, Model model) {
        Candidate candidate = candidateService.findCandidateById(id);
        model.addAttribute(MessageList.Common.CANDIDATE, candidate);
        return "ui/candidate/detail";
    }

    @GetMapping("/edit/{id}")
    public String getEditCandidate(@PathVariable Long id, Model model) {
        Candidate candidate = candidateService.findCandidateById(id);
        List<Long> ids = candidate.getSkillCandidates().stream().map(s -> s.getSkill().getId()).toList();
        model.addAttribute(MessageList.Common.SKILLS, skillService.findAllSkill());
        model.addAttribute(MessageList.Common.SKILLIDS, ids);
        model.addAttribute(MessageList.Common.RECRUITERS, userService.findUserByRoleRecruiter());
        model.addAttribute(MessageList.Common.CANDIDATE, candidate);
        return "ui/candidate/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCandidate(RedirectAttributes ra,
                                @PathVariable Long id,
                                @ModelAttribute CandidateRequest candidateRequest) throws IOException {
        candidateService.updateCandidate(candidateRequest);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageCandidate.EDIT_CANDIDATE_SUCCESS);
        return "redirect:/candidate/edit/" + id;
    }
    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable Long id,
                                  RedirectAttributes ra) {
        candidateService.deleteCandidate(id);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageCandidate.DELETE_CANDIDATE_SUCCESS);
        return "redirect:/candidate";
    }

    @GetMapping("/download/{id}")
    public void download(HttpServletResponse resp, @PathVariable Long id) {
        resp.setContentType("application/octet-stream");
        Candidate candidate = candidateService.findCandidateById(id);
        String keyHeader = "Content-Disposition";
        String valueHeader = "attachment; filename=" + candidate.getCv();
        resp.setHeader(keyHeader, valueHeader);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(fileService.downloadFile(candidate.getId(), candidate.getCv()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Don't download.");
        }
    }
}
