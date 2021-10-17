package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class GetEthBalanceTest {

    @Test
    public void manualTest() throws Exception {
        final GetEthBalance sut = new GetEthBalance();

        final DelegateExecution delEx = mock(DelegateExecution.class);

        sut.execute(delEx);
    }
}