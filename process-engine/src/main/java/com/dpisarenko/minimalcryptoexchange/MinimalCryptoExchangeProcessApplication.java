package com.dpisarenko.minimalcryptoexchange;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class MinimalCryptoExchangeProcessApplication {
    @Autowired
    private ProcessEngine processEngine;

    public static void main(String... args) {
        SpringApplication.run(MinimalCryptoExchangeProcessApplication.class, args);
    }
}
