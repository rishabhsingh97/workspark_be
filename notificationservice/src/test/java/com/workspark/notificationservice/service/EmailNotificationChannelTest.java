package com.workspark.notificationservice.service;

import com.workspark.models.pojo.Email;
import com.workspark.models.pojo.Notification;
import com.workspark.notificationservice.exceptions.customExceptions.EmailException;
import com.workspark.notificationservice.service.impl.EmailNotificationChannel;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class EmailNotificationChannelTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailNotificationChannel emailNotificationChannel;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendEmailWithTemplate() {
        // Arrange
        Email email = new Email();
        email.setFromEmail("sender@example.com");
        email.setFromName("Sender");
        email.setToEmail("recipient@example.com");
        email.setToName("Recipient");
        email.setSubject("Test Subject");
        email.setTemplate("testTemplate");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "Recipient");
        parameters.put("message", "This is a test email.");
        email.setParameters(parameters);

        Notification notification = new Notification();
        notification.setEmail(email);

        Context context = new Context();
        context.setVariable("name", "Recipient");
        context.setVariable("message", "This is a test email.");
        when(templateEngine.process(eq("testTemplate"), any(Context.class)))
                .thenReturn("<p>Hello Recipient, This is a test email.</p>");

        // Act
        boolean result = emailNotificationChannel.send(notification);

        // Assert
        assertTrue(result);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailWithoutTemplate() {
        // Arrange
        Email email = new Email();
        email.setFromEmail("sender@example.com");
        email.setFromName("Sender");
        email.setToEmail("recipient@example.com");
        email.setToName("Recipient");
        email.setSubject("Test Subject");
        email.setMessage("This is a plain text email.");

        Notification notification = new Notification();
        notification.setEmail(email);

        // Act
        boolean result = emailNotificationChannel.send(notification);

        // Assert
        assertTrue(result);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailThrowsExceptionForInvalidDetails() {
        // Arrange
        Email email = new Email();
        email.setFromEmail("invalid-email");
        email.setFromName("Sender");
        email.setToEmail("recipient@example.com");
        email.setToName("Recipient");
        email.setSubject("Test Subject");
        email.setMessage("This is a plain text email.");

        Notification notification = new Notification();
        notification.setEmail(email);

        doThrow(new EmailException("Error setting email details"))
                .when(mailSender).createMimeMessage();

        // Act & Assert
        EmailException exception = assertThrows(
                EmailException.class,
                () -> emailNotificationChannel.send(notification)
        );
        assertTrue(exception.getMessage().contains("Error setting email details"));
    }
}
