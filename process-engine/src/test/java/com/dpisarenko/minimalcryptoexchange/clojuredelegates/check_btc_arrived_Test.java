/*
 * Copyright 2021, 2022 Dmitrii Pisarenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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
