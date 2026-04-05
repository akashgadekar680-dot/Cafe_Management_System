package com.in.cafe.com.in.cafe.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    // Send normal email
    public void sendSimpleMessage(String to, String subject, String text, List<String> ccList) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("uniqueiteam2@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            if (ccList != null && !ccList.isEmpty()) {
                message.setCc(ccList.toArray(new String[0]));
            }

            emailSender.send(message);

            System.out.println("Email sent successfully to: " + to);

        } catch (Exception e) {
            System.out.println("Error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Forgot password email
    public void forgotMail(String to, String subject, String password) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("uniqueiteam2@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlMsg =
                    "<p><b>Your Login Details for Cafe Management System</b></p>"
                    + "<p><b>Email:</b> " + to + "</p>"
                    + "<p><b>Password:</b> " + password + "</p>"
                    + "<p><a href='http://localhost:4200/'>Click here to login</a></p>";

            helper.setText(htmlMsg, true);

            emailSender.send(message);

            System.out.println("Forgot password email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.out.println("Error while sending forgot password email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}