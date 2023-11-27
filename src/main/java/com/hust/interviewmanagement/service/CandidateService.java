package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface CandidateService {
    List<Candidate> findCandidateByStatus(EStatus status);
    Candidate findCandidateById(Long id);
    Candidate saveCandidate(CandidateRequest candidateRequest) throws IOException;
    Page<Candidate> findAllCandidate(SearchRequest searchRequest);

    Candidate updateCandidate(CandidateRequest candidateRequest) throws IOException;

    void deleteCandidate(Long id);
}
