package com.app.service;

import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.enums.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//InMemoryBased for now
//TODO inDB

@Service
@Slf4j
public class PaymentService {

    private Map<Integer, BigDecimal> userBalanceMap;

    @PostConstruct
    private void init() {
        userBalanceMap = new HashMap<>();
        userBalanceMap.put(1, BigDecimal.valueOf(5000));
        userBalanceMap.put(2, BigDecimal.valueOf(1000));
        userBalanceMap.put(3, BigDecimal.valueOf(6000));
    }

    public PaymentResponse debit(final PaymentRequest requestDTO) {
        BigDecimal balance = getBalance(requestDTO.getUserId());
        PaymentResponse response = PaymentResponse.builder()
                .amount(requestDTO.getAmount())
                .userId(requestDTO.getUserId())
                .orderId(requestDTO.getOrderId())
                .status(PaymentStatus.PAYMENT_REJECTED)
                .build();
        if (balance.compareTo(requestDTO.getAmount()) >= 0) {
            response.setStatus(PaymentStatus.PAYMENT_APPROVED);
            userBalanceMap.put(requestDTO.getUserId(), balance.subtract(response.getAmount()));
            log.info("Payment Approved");
        }
        return response;
    }

    public void credit(final PaymentRequest requestDTO) {
        log.info("Payment Rejected");
        userBalanceMap.computeIfPresent(requestDTO.getUserId(), (k, v) -> requestDTO.getAmount().add(v));
    }

    public BigDecimal getBalance(int id) {
       return userBalanceMap.getOrDefault(id, BigDecimal.valueOf(0));
    }
}