package com.app.service;

import com.app.dto.InventoryRequest;
import com.app.dto.OrchestratorRequestDto;
import com.app.dto.OrchestratorResponseDto;
import com.app.dto.PaymentRequestDto;
import com.app.enums.OrderStatus;
import com.app.service.steps.InventoryStep;
import com.app.service.steps.PaymentStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class OrchestratorService {
    @Autowired
    @Qualifier("payment")
    private WebClient paymentClient;

    @Autowired
    @Qualifier("inventory")
    private WebClient inventoryClient;

    public Mono<OrchestratorResponseDto> orderProduct(final OrchestratorRequestDto requestDTO){
        WorkFlow orderWorkflow = this.getOrderWorkflow(requestDTO);
        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                .flatMap(WorkFlowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if(aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkFlowException("create order failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTO(requestDTO, OrderStatus.ORDER_COMPLETED)))
                .onErrorResume(ex -> this.revertOrder(orderWorkflow, requestDTO));
    }

    private Mono<OrchestratorResponseDto> revertOrder(final WorkFlow workflow, final OrchestratorRequestDto requestDTO){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkFlowStepStatus.COMPLETE))
                .flatMap(WorkFlowStep::revert)
                .retry(3)
                .then(Mono.just(this.getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
    }

    private WorkFlow getOrderWorkflow(OrchestratorRequestDto requestDTO){
        WorkFlowStep paymentStep = new PaymentStep(this.paymentClient, this.getPaymentRequestDTO(requestDTO));
        WorkFlowStep inventoryStep = new InventoryStep(this.inventoryClient, this.getInventoryRequestDTO(requestDTO));
        return new OrderWorkFlow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponseDto getResponseDTO(OrchestratorRequestDto requestDTO, OrderStatus status){
        return OrchestratorResponseDto.builder()
                .orderId(requestDTO.getOrderId())
                .amount(requestDTO.getAmount())
                .skuCode(requestDTO.getSkuCode())
                .userId(requestDTO.getUserId())
                .status(status)
                .build();
    }

    private PaymentRequestDto getPaymentRequestDTO(OrchestratorRequestDto requestDTO){
        return PaymentRequestDto.builder()
                .userId(requestDTO.getUserId())
                .amount(requestDTO.getAmount())
                .orderId(requestDTO.getOrderId())
                .build();
    }

    private InventoryRequest getInventoryRequestDTO(OrchestratorRequestDto requestDTO){
        return InventoryRequest.builder()
                .skuCode(requestDTO.getSkuCode())
                .quantity(requestDTO.getQuantity())
                .build();
    }
}
