package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.dpisarenko.minimalcryptoexchange.clj.ClojureService.MAIN_CLOJURE_NAMESPACE;
import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class set_retry_counter_to_0_Test {
    @Test
    public void givenDelegateExecution_whenExecute_thenSetRetryCounterToZero() {
        // Given
        final ClojureService backend = createClojureBackend();

        final DelegateExecution delEx = mock(DelegateExecution.class);

        // When
        backend.runClojureCode(delEx, "set_retry_counter_to_0");

        // Then
        verify(delEx).setVariable("RETRY_COUNTER", 0L);
    }

}
