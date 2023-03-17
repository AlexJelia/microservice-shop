package com.app.service;

import com.app.dto.InventoryRequest;
import com.app.dto.OrchestratorRequest;
import com.app.dto.OrchestratorResponse;
import com.app.dto.PaymentRequest;
import com.app.enums.OrderStatus;
import com.app.service.steps.InventoryStep;
import com.app.service.steps.PaymentStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrchestratorService {
    @Autowired
    @Qualifier("payment")
    private WebClient paymentClient;

    @Autowired
    @Qualifier("inventory")
    private WebClient inventoryClient;

    public Mono<OrchestratorResponse> orderProduct(final OrchestratorRequest requestDTO){
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

    private Mono<OrchestratorResponse> revertOrder(final WorkFlow workflow, final OrchestratorRequest requestDTO){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkFlowStepStatus.COMPLETE))
                .flatMap(WorkFlowStep::revert)
                .retry(3)
                .then(Mono.just(this.getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
    }

    private WorkFlow getOrderWorkflow(OrchestratorRequest requestDTO){
        WorkFlowStep paymentStep = new PaymentStep(this.paymentClient, this.getPaymentRequestDTO(requestDTO));
        WorkFlowStep inventoryStep = new InventoryStep(this.inventoryClient, this.getInventoryRequestDTO(requestDTO));
        return new OrderWorkFlow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponse getResponseDTO(OrchestratorRequest requestDTO, OrderStatus status){
        return OrchestratorResponse.builder()
                .orderId(requestDTO.getOrderId())
                .amount(requestDTO.getAmount())
                .skuCode(requestDTO.getSkuCode())
                .userId(requestDTO.getUserId())
                .status(status)
                .build();
    }

    private PaymentRequest getPaymentRequestDTO(OrchestratorRequest requestDTO){
        return PaymentRequest.builder()
                .userId(requestDTO.getUserId())
                .amount(requestDTO.getAmount())
                .orderId(requestDTO.getOrderId())
                .build();
    }

    private InventoryRequest getInventoryRequestDTO(OrchestratorRequest requestDTO){
        return InventoryRequest.builder()
                .skuCode(requestDTO.getSkuCode())
                .quantity(requestDTO.getQuantity())
                .build();
    }
}
