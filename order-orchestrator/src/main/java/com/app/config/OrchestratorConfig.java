package com.app.config;

import com.app.dto.OrchestratorRequest;
import com.app.dto.OrchestratorResponse;
import com.app.service.OrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class OrchestratorConfig {

    private final OrchestratorService orchestratorService;
    @Bean
    public Function<Flux<OrchestratorRequest>, Flux<OrchestratorResponse>> processor() {
        return orchestratorRequestFlux -> orchestratorRequestFlux.flatMap(this::processEvent);
    }

    private Mono<OrchestratorResponse> processEvent(OrchestratorRequest dto) {
        return orchestratorService.orderProduct(dto);
    }
}