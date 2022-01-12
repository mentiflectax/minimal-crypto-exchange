package com.dpisarenko.minimalcryptoexchange.clj;

import clojure.java.api.Clojure;
import org.bitcoinj.core.Coin;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

@Service
public class ClojureService {

    public static final String MAIN_CLOJURE_NAMESPACE = "com.dpisarenko.core";

    public void runClojureCode(final DelegateExecution delEx, final String clojureFunctionName) {
        Clojure.var(MAIN_CLOJURE_NAMESPACE, clojureFunctionName)
                .invoke(delEx);
    }

    public void btcTxReceived(final String txId, final Coin amount) {
        Clojure.var("com.dpisarenko.core", "btcTxReceived")
                .invoke(txId, amount);
    }
}
