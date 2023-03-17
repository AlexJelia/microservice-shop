package com.app.config;

import com.app.dto.OrchestratorRequestDto;
import com.app.dto.OrchestratorResponseDto;
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
    public Function<Flux<OrchestratorRequestDto>, Flux<OrchestratorResponseDto>> processor() {
        return orchestratorRequestFlux -> orchestratorRequestFlux.flatMap(this::processEvent);
    }

    private Mono<OrchestratorResponseDto> processEvent(OrchestratorRequestDto dto) {
        return orchestratorService.orderProduct(dto);
    }
}