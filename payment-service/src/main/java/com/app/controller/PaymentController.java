package com.app.controller;


import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/debit")
    public PaymentResponse debit(@RequestBody PaymentRequest requestDTO) {
        log.info("CALL DEBIT CONTROLLER");
        return this.service.debit(requestDTO);
    }

    @PostMapping("/credit")
    public void credit(@RequestBody PaymentRequest requestDTO) {
        log.info("CALL CREDIT CONTROLLER");
        this.service.credit(requestDTO);
    }

    @GetMapping("balance/{id}")
    public BigDecimal getBalance(@PathVariable int id){
       return service.getBalance(id);
    }

}