package com.app.notificationservice.event;

import com.app.dto.OrchestratorResponse;
import com.app.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class OrderPlacedEventConsumer {
    @Autowired
    private NotificationService service;

    @Bean
    public Consumer<OrchestratorResponse> eventConsumer() {
        //listen topic
        return (orchestratorResponse) -> service.handleEvent(orchestratorResponse);
    }
}