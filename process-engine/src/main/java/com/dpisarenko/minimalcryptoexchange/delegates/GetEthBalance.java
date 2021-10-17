package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEthBalance implements JavaDelegate {
    private final Logger logger;

    GetEthBalance(Logger logger) {
        this.logger = logger;
    }

    public GetEthBalance() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.debug("Hello!");
    }
}
