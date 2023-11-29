package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendMailSimple(EmailDetail emailDetail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(emailDetail.getRecipient());
        message.setSubject(emailDetail.getSubject());
        message.setText(emailDetail.getMsgBody());
        javaMailSender.send(message);
    }

    @Override
    public void sendMailHtml(EmailDetail emailDetail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(sender);
        messageHelper.setTo(emailDetail.getRecipient());
        messageHelper.setSubject(emailDetail.getSubject());
        messageHelper.setText(emailDetail.getMsgBody(), true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    @Async("taskExecutor")
    public void sendMailNotificationInterviewSchedule(Collection<String> email, String subject, InterviewSchedule interviewSchedule) throws MessagingException {
        for (String e : email) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>")
                    .append("<h5>").append(subject).append("</h5>")
                    .append("<h6>Time: </h6> <span>").append(interviewSchedule.getSchedule()).append("</span>")
                    .append("</br>")
                    .append("<h6>Location: </h6> <span>").append(interviewSchedule.isLocation() ? "Online" : "Offline").append("</span>")
                    .append("</br>")
                    .append("<h6>Meeting: </h6> <span>").append(interviewSchedule.getMeeting()).append("</span>")
                    .append("</br>")
                    .append("</div>");
            EmailDetail emailDetail = EmailDetail.builder()
                    .recipient(e)
                    .subject(subject)
                    .msgBody(sb.toString())
                    .build();
            sendMailHtml(emailDetail);
        }
    }

    @Override
    public void sendMailCancelInterviewSchedule(Collection<String> email) throws MessagingException {
        for (String e : email) {
            EmailDetail emailDetail = EmailDetail.builder()
                    .recipient(e)
                    .subject("Cancel Interview schedule")
                    .msgBody("Sorry!")
                    .build();
            sendMailHtml(emailDetail);
        }
    }


    @Override
    @Async("taskExecutor")
    public void sendMailToUser(Account account, String password) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>")
                .append("<h4>Information Your Account</h5>")
                .append("<h5>Email: </h6> <span>").append(account.getEmail()).append("</span>")
                .append("</br>")
                .append("<h5>Password: </h6> <span>").append(password).append("</span>")
                .append("</br>")
                .append("<h5>Role: </h6> <span>").append(account.getRole().getRole()).append("</span>")
                .append("</br>")
                .append("</div>");
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(account.getEmail())
                .subject("Information Account")
                .msgBody(sb.toString())
                .build();
        sendMailHtml(emailDetail);
    }


}
