package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.*;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;

import java.util.Collection;
import java.util.List;

public interface EmailService {
    void sendMailSimple(EmailDetail emailDetail) throws MessagingException;
    void sendMailHtml(EmailDetail emailDetail   ) throws MessagingException;
    void sendMailInterviewScheduleToCandidate(ResultInterview resultInterview) throws MessagingException;
    void sendMailCancelInterviewScheduleToCandidate(ResultInterview resultInterview) throws MessagingException;
    void sendMailUpdateInterviewScheduleToCandidate(ResultInterview resultInterview) throws MessagingException;
    void sendMailResultToCandidate(ResultInterview resultInterview) throws MessagingException;
    void sendMailInterviewScheduleToInterviewer(Collection<InterviewerSchedule> interviewerSchedules) throws MessagingException;
    void sendMailCancelInterviewScheduleToInterviewer(Collection<InterviewerSchedule> interviewerSchedules) throws MessagingException;
    void sendMailUpdateInterviewScheduleToInterviewer(Collection<InterviewerSchedule> interviewerSchedules) throws MessagingException;
    void sendMailUpdateInterviewScheduleToInterviewer(Collection<Users> users,InterviewSchedule interviewSchedule) throws MessagingException;
    void sendMailToUser(Account account,String password) throws MessagingException;

}
