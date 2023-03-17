package com.app.notificationservice.service.email;

import com.app.dto.OrchestratorResponse;
import com.app.notificationservice.dto.EmailContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final SpringTemplateEngine templateEngine;

    public EmailContent generate(OrchestratorResponse response){
        Context context = new Context();
        context.setVariable("orderId",response.getOrderId());
        context.setVariable("productName",response.getSkuCode());
        context.setVariable("orderStatus",response.getStatus());

        return EmailContent.builder()
                .text(templateEngine.process("order-status.txt",context))
                .html(templateEngine.process("order-status.html",context))
                .build();
    }
}
