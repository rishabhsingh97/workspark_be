package com.workspark.notificationservice.service.impl;

import com.workspark.models.pojo.Email;
import com.workspark.models.pojo.Notification;
import com.workspark.notificationservice.exceptions.customExceptions.EmailException;
import com.workspark.notificationservice.service.NotificationChannel;

import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public boolean send(Notification notification) {
        Email email = notification.getEmail();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(new InternetAddress(email.getFromEmail(), email.getFromName()));
            helper.setTo(new InternetAddress(email.getToEmail(), email.getToName()));
            helper.setSubject(email.getSubject());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Error setting email details" + e.getMessage());
        }

        String body;
            boolean isHtml = false;
            if (StringUtils.hasText(email.getTemplate())) {
                isHtml = true;
                Context context = new Context();
                email.getParameters().forEach(context::setVariable);
                body = templateEngine.process(email.getTemplate(), context);
            } else {
                body = email.getMessage();
            }
        try {
            helper.setText(body, isHtml);
        } catch (MessagingException e) {
            throw new EmailException("Error setting email body" + e.getMessage());
        }

        mailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", email.getToEmail());
            return true;
        }
    }

