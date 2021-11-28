package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("clj-del")
public class ClojureDelegate implements JavaDelegate {
    private final ClojureService backend;

    public ClojureDelegate(final ClojureService backend) {
        this.backend = backend;
    }

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        final String clojureFunctionName = (String) delEx.getVariable("cljfn");
        backend.runClojureCode(delEx, clojureFunctionName);
    }
}
