package com.app.service.steps;

import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.enums.PaymentStatus;
import com.app.service.WorkFlowStep;
import com.app.service.WorkFlowStepStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class PaymentStep implements WorkFlowStep {

    private final WebClient.Builder webClient;
    private final PaymentRequest requestDTO;
    private WorkFlowStepStatus stepStatus = WorkFlowStepStatus.PENDING;

    public PaymentStep(WebClient.Builder webClient, PaymentRequest requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkFlowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        log.info("CALL DEBIT PAYMENT");
        return this.webClient.build()
                .post()
                .uri("http://payment-service/api/payment/debit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .map(r -> r.getStatus().equals(PaymentStatus.PAYMENT_APPROVED))
                .doOnNext(b -> this.stepStatus = b ? WorkFlowStepStatus.COMPLETE : WorkFlowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        log.info("CALL REVERT PAYMENT");
        return this.webClient.build()
                .post()
                .uri("http://payment-service/api/payment/credit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }
}