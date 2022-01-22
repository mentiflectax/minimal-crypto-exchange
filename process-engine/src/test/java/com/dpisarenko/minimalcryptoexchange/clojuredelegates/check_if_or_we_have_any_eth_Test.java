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
