/*
 * Copyright 2021, 2022 Dmitrii Pisarenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Coin;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.slf4j.Logger;

import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.createClojureBackend;
import static com.dpisarenko.minimalcryptoexchange.clojuredelegates.TestUtils.initClojureBackend;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class get_received_satoshis_Test {
    @Test
    public void givenDelegateExecution_whenExecute_thenSetReceivedSatoshisVariable() {
        // Given
        final Logger logger = mock(Logger.class);
        final ClojureService backend = createClojureBackend();
        initClojureBackend(logger);

        backend.btcTxReceived("txId", Coin.valueOf(1000L));

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("INCOMING_TX_ID")).thenReturn("txId");

        // When
        backend.runClojureCode(delEx, "get_received_satoshis");

        // Then
        verify(delEx).setVariable("RECEIVED_SATOSHIS", 1000L);
    }
}
