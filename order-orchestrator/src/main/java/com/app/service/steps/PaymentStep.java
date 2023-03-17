package com.app.service.steps;

import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.enums.PaymentStatus;
import com.app.service.WorkFlowStep;
import com.app.service.WorkFlowStepStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class PaymentStep implements WorkFlowStep {

    private final WebClient webClient;
    private final PaymentRequest requestDTO;
    private WorkFlowStepStatus stepStatus = WorkFlowStepStatus.PENDING;

    public PaymentStep(WebClient webClient, PaymentRequest requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkFlowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return this.webClient
                .post()
                .uri("/api/payment/debit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .map(r -> r.getStatus().equals(PaymentStatus.PAYMENT_APPROVED))
                .doOnNext(b -> this.stepStatus = b ? WorkFlowStepStatus.COMPLETE : WorkFlowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
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