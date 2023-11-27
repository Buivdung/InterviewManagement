package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.InterviewSchedule;
import com.hust.interviewmanagement.web.request.InterviewRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface InterviewService {
    Page<InterviewSchedule> findAllInterviewSchedule(SearchRequest interviewSearch);

    InterviewSchedule saveInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException;

    InterviewSchedule updateInterviewSchedule(InterviewSchedule interviewSchedule, InterviewRequest interviewRequest) throws MessagingException;

    InterviewSchedule saveInterviewSchedule(InterviewSchedule interviewSchedule);

    void deleteInterviewScheduleById(Long id) throws MessagingException;

    InterviewSchedule findByInterviewScheduleById(Long id);
}
