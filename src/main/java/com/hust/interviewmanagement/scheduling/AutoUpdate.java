package com.hust.interviewmanagement.scheduling;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.InterviewScheduleRepository;
import com.hust.interviewmanagement.service.EmailService;
import com.hust.interviewmanagement.service.InterviewService;
import com.hust.interviewmanagement.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AutoUpdate {

    final private InterviewScheduleRepository interviewScheduleRepository;
    final private EmailService emailService;
    final private UserService userService;

    @Scheduled(cron = "01 00 00 * * *")
    public void updateStatusInterviewerSchedule() {
        LocalDate localDate = LocalDate.now();
        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findAllBySchedule(localDate);
        interviewSchedules.forEach(x -> {
            x.setStatus(EStatus.IN_PROGRESS);
            x.getResultInterviews().getCandidate().setStatus(EStatus.IN_PROGRESS);
        });
        interviewScheduleRepository.saveAll(interviewSchedules);
    }

    @Scheduled(cron = "01 00 08 * * *")
    public void sendMailRemind() throws MessagingException {
        LocalDate localDate = LocalDate.now();
        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findAllBySchedule(localDate);
        Set<InterviewerSchedule> interviewers = new HashSet<>();
        interviewSchedules.forEach(x -> interviewers.addAll(x.getInterviewer()));
        emailService.sendMailInterviewScheduleToInterviewer(interviewers);
    }
}
