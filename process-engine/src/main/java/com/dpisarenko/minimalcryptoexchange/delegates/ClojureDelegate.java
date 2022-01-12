package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ClojureDelegate")
public class ClojureDelegate implements JavaDelegate {
    @Autowired
    private ClojureService backend;

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        final String clojureFunctionName = (String) delEx.getVariable("cljfn");
        backend.runClojureCode(delEx, clojureFunctionName);
    }
}
