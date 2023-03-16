package com.app.service.steps;

import com.app.dto.PaymentRequestDto;
import com.app.dto.PaymentResponseDto;
import com.app.enums.PaymentStatus;
import com.app.service.WorkFlowStep;
import com.app.service.WorkFlowStepStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Slf4j
public class PaymentStep implements WorkFlowStep {

    private final WebClient webClient;
    private final PaymentRequestDto requestDTO;
    private WorkFlowStepStatus stepStatus = WorkFlowStepStatus.PENDING;

    public PaymentStep(WebClient webClient, PaymentRequestDto requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkFlowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        log.info("CALL PAYMENT");
        return this.webClient
                .post()
                .uri("/api/payment/debit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(PaymentResponseDto.class)
                .map(r -> r.getStatus().equals(PaymentStatus.PAYMENT_APPROVED))
                .doOnNext(b -> this.stepStatus = b ? WorkFlowStepStatus.COMPLETE : WorkFlowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        log.info("REVERT FROM PAYMENT");
        return this.webClient
                .post()
                .uri("/api/payment/credit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }

}