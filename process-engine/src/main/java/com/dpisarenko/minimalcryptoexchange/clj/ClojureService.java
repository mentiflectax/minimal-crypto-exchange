package com.dpisarenko.minimalcryptoexchange.clj;

import clojure.java.api.Clojure;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.wallet.Wallet;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class ClojureService {
    BigDecimal add(BigDecimal a, BigDecimal b) {
        return (BigDecimal) Clojure.var("com.dpisarenko.camundarepl", "add").invoke(a, b);
    }

    void runClojureCode(final DelegateExecution delEx, final String clojureFunctionName) {
        Clojure.var("com.dpisarenko.camundarepl", clojureFunctionName)
                .invoke(delEx);
    }

    public void btcTxReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
        Clojure.var("com.dpisarenko.core", "btcTxReceived")
                .invoke(wallet, tx, prevBalance, newBalance);
    }

    @PostConstruct
    void test() {
        btcTxReceived(null, null, null, null);
    }
}
