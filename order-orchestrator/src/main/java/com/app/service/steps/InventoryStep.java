package com.app.service.steps;

import com.app.dto.InventoryRequest;
import com.app.dto.InventoryResponse;
import com.app.enums.InventoryStatus;
import com.app.service.WorkFlowStep;
import com.app.service.WorkFlowStepStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Slf4j
public class InventoryStep implements WorkFlowStep {

    private final WebClient webClient;
    private final InventoryRequest requestDTO;
    private WorkFlowStepStatus stepStatus = WorkFlowStepStatus.PENDING;

    public InventoryStep(WebClient webClient, InventoryRequest requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkFlowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        log.info("CALL DEDUCT FROM INVENTORY");
        return this.webClient
                .post()
                .uri("/api/inventory/deduct")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .map(r -> r.getStatus().equals(InventoryStatus.AVAILABLE))
                .doOnNext(b -> this.stepStatus = b ? WorkFlowStepStatus.COMPLETE : WorkFlowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        log.info("CALL REVERT INVENTORY");
        return this.webClient
                .post()
                .uri("/api/inventory/revert")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r ->true)
                .onErrorReturn(false);
    }

}
