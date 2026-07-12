package com.hospital.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            log.info("Email fallback log to {} | subject: {} | body: {}", to, subject, body);
        }
    }

    public void sendPasswordResetEmail(String to, String token) {
        sendEmail(to, "Password Reset", "Use this token to reset your password: " + token);
    }

    public void sendTemplateEmail(String to, String templateKey, String subject, Map<String, String> variables) {
        StringBuilder body = new StringBuilder("Template: ").append(templateKey).append("\n");
        variables.forEach((k, v) -> body.append(k).append(": ").append(v).append("\n"));
        sendEmail(to, subject, body.toString());
    }
}
