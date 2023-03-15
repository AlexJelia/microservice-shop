package com.app.service;

import reactor.core.publisher.Mono;

public interface WorkFlowStep {
    WorkFlowStepStatus getStatus();

    Mono<Boolean> process();

    Mono<Boolean> revert();
}
