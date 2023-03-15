package com.app.service;

import com.app.dto.PaymentRequestDto;
import com.app.dto.PaymentResponseDto;
import com.app.enums.PaymentStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//InMemoryBased for now
//TODO inDB

@Service
public class PaymentService {

    private Map<Integer, BigDecimal> userBalanceMap;

    @PostConstruct
    private void init(){
        this.userBalanceMap = new HashMap<>();
        this.userBalanceMap.put(1, BigDecimal.valueOf(1000));
        this.userBalanceMap.put(2, BigDecimal.valueOf(1000));
        this.userBalanceMap.put(3, BigDecimal.valueOf(1000));
    }

    public PaymentResponseDto debit(final PaymentRequestDto requestDTO){
        BigDecimal balance = this.userBalanceMap.getOrDefault(requestDTO.getUserId(), BigDecimal.valueOf(0));
        PaymentResponseDto response = PaymentResponseDto.builder()
                .amount(requestDTO.getAmount())
                .userId(requestDTO.getUserId())
                .orderId(requestDTO.getOrderId())
                .status(PaymentStatus.PAYMENT_REJECTED)
                .build();
        if(balance.compareTo(requestDTO.getAmount()) >=0) {
            response.setStatus(PaymentStatus.PAYMENT_APPROVED);
            this.userBalanceMap.put(requestDTO.getUserId(), balance.subtract(response.getAmount()));
        }
        return response;
    }

    public void credit(final PaymentRequestDto requestDTO){
        this.userBalanceMap.computeIfPresent(requestDTO.getUserId(), (k, v) ->requestDTO.getAmount().add(v));
    }

}