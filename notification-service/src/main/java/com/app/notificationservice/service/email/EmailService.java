package com.app.notificationservice.service.email;

import com.app.notificationservice.dto.EmailContent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.addresses.from}")
    private String emailFrom;

    @Value("${spring.mail.addresses.to}")
    private String replyTo;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String recipient, String subject, EmailContent content) throws MailException{
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
            messageHelper.setFrom(emailFrom);
            messageHelper.setReplyTo(replyTo);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(content.getText(), content.getHtml());
        };
        emailSender.send(messagePreparator);
    }
}
