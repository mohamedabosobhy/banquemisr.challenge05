package com.task.management.service;


import com.task.management.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testSendEmail() throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage((javax.mail.Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String[] to = {"test@example.com"};
        String subject = "Test Subject";
        String text = "Test Text";

        emailService.sendEmail(to, subject, text);

        verify(mailSender).send(any(MimeMessage.class));
    }
}
