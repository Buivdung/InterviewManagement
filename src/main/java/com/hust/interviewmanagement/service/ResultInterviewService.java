package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.ResultInterview;

public interface ResultInterviewService {
    ResultInterview findResultInterviewByCandidate_Id(Long id);
}
