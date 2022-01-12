package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import clojure.java.api.Clojure;
import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Coin;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collection;

import static com.dpisarenko.minimalcryptoexchange.clj.ClojureService.MAIN_CLOJURE_NAMESPACE;
import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(value = Parameterized.class)
public class check_btc_arrived_Test {
    private final boolean addTx;
    private final boolean expectedResult;

    public check_btc_arrived_Test(boolean addTx, boolean expectedResult) {
        this.addTx = addTx;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {false, false},
                {true, true},
        });
    }

    @Test
    public void givenState_whenExecute_thenSetBtcArrivedToCorrectValue() {
        // Given
        final Logger logger = mock(Logger.class);
        final ClojureService backend = createClojureBackend();
        Clojure.var(MAIN_CLOJURE_NAMESPACE, "init")
                .invoke(logger);
        if (addTx) {
            backend.btcTxReceived("txId", Coin.valueOf(1000L));
        }

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("INCOMING_TX_ID")).thenReturn("txId");

        // When
        backend.runClojureCode(delEx, "check-btc-arrived");

        // Then
        verify(delEx).setVariable("BTC_ARRIVED", expectedResult);
    }

}
