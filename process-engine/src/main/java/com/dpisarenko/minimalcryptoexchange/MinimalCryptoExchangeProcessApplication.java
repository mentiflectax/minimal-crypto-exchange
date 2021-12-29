package com.dpisarenko.minimalcryptoexchange;

import com.dpisarenko.minimalcryptoexchange.delegates.TransferUsdtToExchangeAccount;
import com.dpisarenko.minimalcryptoexchange.logic.btc.WalletObserver;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableProcessApplication
public class MinimalCryptoExchangeProcessApplication {
    @Autowired
    private ProcessEngine processEngine;

    public static void main(String... args) {
        SpringApplication.run(MinimalCryptoExchangeProcessApplication.class, args);
    }
}
