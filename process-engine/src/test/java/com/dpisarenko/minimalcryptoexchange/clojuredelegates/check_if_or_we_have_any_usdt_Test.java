package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(value = Parameterized.class)
public class check_if_or_we_have_any_usdt_Test {
    private final long usdtExchangeBalance;
    private final boolean anyUsdtAvailable;

    public check_if_or_we_have_any_usdt_Test(final long usdtExchangeBalance, final boolean anyUsdtAvailable) {
        this.usdtExchangeBalance = usdtExchangeBalance;
        this.anyUsdtAvailable = anyUsdtAvailable;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, false},
                {1, true},
        });
    }
    @Test
    public void givenUsdtExchangeBalance_whenExecute_thenSetAnyUsdtAvailableVariableToCorrectValue() {
        // Given
        final ClojureService backend = createClojureBackend();

        final DelegateExecution delEx = mock(DelegateExecution.class);
        when(delEx.getVariable("USDT_EXCHANGE_BALANCE")).thenReturn(usdtExchangeBalance);

        // When
        backend.runClojureCode(delEx, "check_if_or_we_have_any_usdt");

        // Then
        verify(delEx).setVariable("ANY_USDT_AVAILABLE", anyUsdtAvailable);
    }
}
