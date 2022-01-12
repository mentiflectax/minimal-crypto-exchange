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
public class check_max_number_of_wait_cycles_exceeded_Test {
    private final long retryCounter;
    private final boolean maxRetriesExceeded;

    public check_max_number_of_wait_cycles_exceeded_Test(final long retryCounter, final boolean maxRetriesExceeded) {
        this.retryCounter = retryCounter;
        this.maxRetriesExceeded = maxRetriesExceeded;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, false},
                {1, false},
                {2, false},
                {3, false},
                {4, false},
                {5, false},
                {6, true},
        });
    }

    @Test
    public void givenRetryCounter_whenExecute_thenReturnCorrectValue() {
        // Given
        final ClojureService backend = createClojureBackend();

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("RETRY_COUNTER")).thenReturn(retryCounter);

        // When
        backend.runClojureCode(delEx, "check_max_number_of_wait_cycles_exceeded");

        // Then
        verify(delEx).setVariable("MAX_NUMBER_OF_WAITING_CYCLES_EXCEEDED", maxRetriesExceeded);
    }
}
