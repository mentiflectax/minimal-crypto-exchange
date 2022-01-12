package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class calculate_usd_amount_Test {
    @Test
    public void givenSatoshis_whenExecute_thenSetUsdAmountVariableToCorrectValue() {
        // Given
        final ClojureService backend = createClojureBackend();

        final DelegateExecution delEx = mock(DelegateExecution.class);
        when(delEx.getVariable("RECEIVED_SATOSHIS")).thenReturn(1000L);

        // When
        backend.runClojureCode(delEx, "calculate_usd_amount");

        // Then
        verify(delEx).setVariable(eq("USD_AMOUNT"), AdditionalMatchers.eq(0.4916999, 1./1000000.));
    }
}
