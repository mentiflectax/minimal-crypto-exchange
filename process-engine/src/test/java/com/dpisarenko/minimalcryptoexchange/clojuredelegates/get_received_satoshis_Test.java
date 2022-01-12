package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import clojure.java.api.Clojure;
import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Coin;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.slf4j.Logger;

import static com.dpisarenko.minimalcryptoexchange.clj.ClojureService.MAIN_CLOJURE_NAMESPACE;
import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class get_received_satoshis_Test {
    @Test
    public void givenDelegateExecution_whenExecute_thenSetReceivedSatoshisVariable() {
        // Given
        final Logger logger = mock(Logger.class);
        final ClojureService backend = createClojureBackend();

        Clojure.var(MAIN_CLOJURE_NAMESPACE, "init")
                .invoke(logger);

        backend.btcTxReceived("txId", Coin.valueOf(1000L));

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("INCOMING_TX_ID")).thenReturn("txId");

        // When
        backend.runClojureCode(delEx, "get_received_satoshis");

        // Then
        verify(delEx).setVariable("RECEIVED_SATOSHIS", 1000L);
    }
}
