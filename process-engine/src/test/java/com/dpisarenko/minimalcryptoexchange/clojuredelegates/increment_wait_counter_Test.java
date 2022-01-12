package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;

import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class increment_wait_counter_Test {
    @Test
    public void givenDelegateExecution_whenExecute_thenIncreaseRetryCounter() {
        // Given
        final ClojureService backend = createClojureBackend();

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("RETRY_COUNTER")).thenReturn(0L);

        // When
        backend.runClojureCode(delEx, "increment_wait_counter");

        // Then
        verify(delEx).setVariable("RETRY_COUNTER", 1L);
    }
}
