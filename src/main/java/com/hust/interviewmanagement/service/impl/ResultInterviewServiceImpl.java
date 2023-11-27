package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.ResultInterview;
import com.hust.interviewmanagement.repository.ResultInterviewRepository;
import com.hust.interviewmanagement.service.ResultInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultInterviewServiceImpl implements ResultInterviewService {
    private final ResultInterviewRepository resultInterviewRepository;

    @Override
    public ResultInterview findResultInterviewByCandidate_Id(Long id) {
        return resultInterviewRepository.findByCandidate_Id(id).orElseThrow();

    }

}
