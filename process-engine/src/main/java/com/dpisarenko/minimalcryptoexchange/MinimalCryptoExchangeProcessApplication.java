package com.dpisarenko.minimalcryptoexchange;

import com.dpisarenko.minimalcryptoexchange.logic.btc.WalletObserver;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableProcessApplication
public class MinimalCryptoExchangeProcessApplication {
    public static void main(String... args) {
        SpringApplication.run(MinimalCryptoExchangeProcessApplication.class, args);
    }

    @PostConstruct
    void startWalletObserver() {
        final WalletObserver walletObserver = new WalletObserver();
        walletObserver.init();
    }
}
