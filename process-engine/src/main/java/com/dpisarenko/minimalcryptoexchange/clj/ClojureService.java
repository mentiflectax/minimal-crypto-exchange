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

    public void runClojureCode(final DelegateExecution delEx, final String clojureFunctionName) {
        Clojure.var("com.dpisarenko.camundarepl", clojureFunctionName)
                .invoke(delEx);
    }

    public void btcTxReceived(final String txId, final Coin amount) {
        Clojure.var("com.dpisarenko.core", "btcTxReceived")
                .invoke(txId, amount);
    }

    @PostConstruct
    void test() {
        //btcTxReceived(null, null, null, null);
    }
}
