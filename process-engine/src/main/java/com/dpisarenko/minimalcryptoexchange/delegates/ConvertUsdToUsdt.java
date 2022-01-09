package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component("ConvertUsdToUsdt")
public class ConvertUsdToUsdt implements JavaDelegate {

    static final long USD_TO_USDT_CONVERSION_FACTOR = 1000000000000000000L;

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        final Double usdAmount = (Double) delEx.getVariable("USD_AMOUNT");
        final long usdtAmountLong = (long) (usdAmount * USD_TO_USDT_CONVERSION_FACTOR);
        final BigInteger usdtAmount = BigInteger.valueOf(usdtAmountLong);
        delEx.setVariable("USDT_AMOUNT", usdtAmount);
    }
}
