package com.mathmastery.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String verificationToken) {
        String subject = "Email Verification - Math Mastery";
        String url = "http://localhost:8080/verify?token=" + verificationToken;
        String content = "<p>Hello,</p>"
                + "<p>Please verify your email by clicking the link below:</p>"
                + "<p><a href=\"" + url + "\">Verify Email</a></p>"
                + "<p>If you did not sign up, please ignore this email.</p>";

        tryAndCatch(to, subject, content);
    }

    public void sendUserIsVerifiedEmail(String to) {
        String subject = "Email Verified - Math Mastery";
        String content = "<p>Hello,</p>"
                + "<p>Your email has been successfully verified.</p>"
                + "<p>Thank you for signing up!</p>";

        tryAndCatch(to, subject, content);
    }

    public void sendSuccessNotification(String to) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Email Updated");
        mail.setText("Your email was successfully updated and verified.");
        mailSender.send(mail);
    }


    private void tryAndCatch(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


}
