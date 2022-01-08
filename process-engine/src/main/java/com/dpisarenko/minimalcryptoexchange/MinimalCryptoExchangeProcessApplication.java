package com.dpisarenko.minimalcryptoexchange;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class MinimalCryptoExchangeProcessApplication {
    public static void main(String... args) {
        SpringApplication.run(MinimalCryptoExchangeProcessApplication.class, args);
    }
}
