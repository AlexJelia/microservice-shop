package com.app.controller;


import com.app.dto.PaymentRequestDto;
import com.app.dto.PaymentResponseDto;
import com.app.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/debit")
    public PaymentResponseDto debit(@RequestBody PaymentRequestDto requestDTO){
        log.info("CALL DEBIT CONTROLLER");
        return this.service.debit(requestDTO);
    }

    @PostMapping("/credit")
    public void credit(@RequestBody PaymentRequestDto requestDTO){
        log.info("CALL CREDIT CONTROLLER");
        this.service.credit(requestDTO);
    }

}