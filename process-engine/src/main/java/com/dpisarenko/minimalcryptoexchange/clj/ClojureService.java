package com.dpisarenko.minimalcryptoexchange.clj;

import clojure.java.api.Clojure;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

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
}
