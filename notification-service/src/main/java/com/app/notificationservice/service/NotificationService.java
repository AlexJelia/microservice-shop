package com.app.notificationservice.service;


import com.app.dto.OrchestratorResponse;
import com.app.notificationservice.service.email.EmailService;
import com.app.notificationservice.service.email.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final TemplateService templateService;

    public OrchestratorResponse handleEvent(OrchestratorResponse payload) {
        System.out.println("RECEIVED MESSAGE FROM KAFKA");
        try {
            emailService.sendEmail(
                    "email adres",
                    "Order status",
                    templateService.generate(payload)
            );
        } catch (MailException e) {
            log.error("Cant send email", e);
        }
        return payload;
    }
}
