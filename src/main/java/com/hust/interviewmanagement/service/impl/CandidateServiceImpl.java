package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.entities.Skill;
import com.hust.interviewmanagement.entities.SkillCandidate;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.CandidateRepository;
import com.hust.interviewmanagement.repository.SkillCandidateRepository;
import com.hust.interviewmanagement.repository.SkillRepository;
import com.hust.interviewmanagement.repository.UserRepository;
import com.hust.interviewmanagement.service.CandidateService;
import com.hust.interviewmanagement.service.FileService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final FileService<Candidate> fileService;
    private final SkillCandidateRepository skillCandidateRepository;

    @Override
    public List<Candidate> findCandidateByStatus(EStatus status) {
        return candidateRepository.findAllByStatus(status);
    }

    @Override
    public Candidate findCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageCandidate.DONT_CANDIDATE));
    }

    @Override
    @Transactional
    public Candidate saveCandidate(CandidateRequest candidateRequest) throws IOException {
        Candidate candidate = candidateRepository.save(getCandidate(new Candidate(), candidateRequest));
        fileService.saveFile(candidateRequest.getCv(), candidate.getId());
        return candidate;
    }

    @Override
    public Page<Candidate> findAllCandidate(SearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber() - 1, SearchRequest.PAGE_SIZE);
        if (searchRequest.getStatus() == null) {
            return candidateRepository.findAll(searchRequest.getParam(), pageable);
        }
        return candidateRepository.findAll(searchRequest.getParam(), searchRequest.getStatus(), pageable);
    }

    @Override
    @Transactional
    public Candidate updateCandidate(CandidateRequest candidateRequest) throws IOException {
        skillCandidateRepository.deleteAllByCandidateId(candidateRequest.getId());
        Candidate candidate = getCandidate(findCandidateById(candidateRequest.getId()), candidateRequest);
        candidateRepository.save(candidate);
        if (candidateRequest.getCv() != null && !candidateRequest.getCv().isEmpty()) {
            fileService.saveFile(candidateRequest.getCv(), candidate.getId());
        }
        return candidate;
    }

    @Override
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    private Candidate getCandidate(Candidate candidate, CandidateRequest candidateRequest) {
        modelMapper.map(candidateRequest, candidate);
        Users recruiter = userRepository.findById(candidateRequest.getRecruiterId()).orElseThrow();
        List<Skill> skills = skillRepository.findAllById(candidateRequest.getSkills());
        List<SkillCandidate> skillCandidates = new ArrayList<>();
        for (Skill s : skills) {
            skillCandidates.add(SkillCandidate.builder().skill(s).candidate(candidate).build());
        }
        if (candidateRequest.getCv() != null && !candidateRequest.getCv().isEmpty()) {
            candidate.setCv(candidateRequest.getCv().getOriginalFilename());
        }
        candidate.setSkillCandidates(skillCandidates);
        candidate.setRecruiter(recruiter);
        return candidate;
    }


}
