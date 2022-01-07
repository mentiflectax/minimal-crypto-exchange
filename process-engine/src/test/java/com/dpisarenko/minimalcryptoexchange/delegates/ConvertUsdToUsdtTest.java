package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;

import java.math.BigInteger;

import static com.dpisarenko.minimalcryptoexchange.delegates.ConvertUsdToUsdt.USD_TO_USDT_CONVERSION_FACTOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConvertUsdToUsdtTest {
    @Test
    public void givenUsdAmount_whenExecute_thenReturnCorrectUsdtAmount() throws Exception {
        // Given
        final ConvertUsdToUsdt sut = new ConvertUsdToUsdt();
        final Double usdAmount = 4.39;
        final long usdtAmountLong = (long)(4.39 * USD_TO_USDT_CONVERSION_FACTOR);
        final BigInteger usdtAmount = BigInteger.valueOf(usdtAmountLong);

        final DelegateExecution delEx = mock(DelegateExecution.class);
        when(delEx.getVariable("USD_AMOUNT")).thenReturn(usdAmount);

        // When
        sut.execute(delEx);

        // Then
        verify(delEx).getVariable("USD_AMOUNT");
        verify(delEx).setVariable("USDT_AMOUNT", usdtAmount);

    }

}