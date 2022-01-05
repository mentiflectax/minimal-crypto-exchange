package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component("ConvertUsdToUsdt")
public class ConvertUsdToUsdt implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        final Double usdAmount = (Double) delegateExecution.getVariable("USD_AMOUNT");
        final long usdtAmountLong = (long) (usdAmount * 1000000000000000000L);
        final BigInteger usdtAmount = BigInteger.valueOf(usdtAmountLong);
        delegateExecution.setVariable("USDT_AMOUNT", usdtAmount);
    }
}
