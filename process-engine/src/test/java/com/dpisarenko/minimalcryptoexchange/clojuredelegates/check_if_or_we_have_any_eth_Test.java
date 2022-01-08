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
public class check_if_or_we_have_any_eth_Test {
    private final long exchangeAccountBalanceWei;
    private final boolean anyEthAvailable;

    public check_if_or_we_have_any_eth_Test(long exchangeAccountBalanceWei, boolean anyEthAvailable) {
        this.exchangeAccountBalanceWei = exchangeAccountBalanceWei;
        this.anyEthAvailable = anyEthAvailable;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, false},
                {1, true},
        });
    }

    @Test
    public void givenExchangeAccountBalance_whenExecute_thenSetAnyEthAvailableToCorrectValue() {
        // Given
        final ClojureService backend = createClojureBackend();

        final DelegateExecution delEx = mock(DelegateExecution.class);
        when(delEx.getVariable("EXCHANGE_ACCOUNT_BALANCE_WEI")).thenReturn(exchangeAccountBalanceWei);

        // When
        backend.runClojureCode(delEx, "check_if_or_we_have_any_eth");

        // Then
        verify(delEx).setVariable("ANY_ETH_AVAILABLE", anyEthAvailable);
    }
}
