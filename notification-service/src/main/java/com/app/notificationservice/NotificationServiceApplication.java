package com.app.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics="${topic.name.notification}")
    public void handleNotification(OrderPlacedEvent event){
        //TODO send email
        log.info("Received Notification for Order : {}",event.getOrderNumber());
    }

}
