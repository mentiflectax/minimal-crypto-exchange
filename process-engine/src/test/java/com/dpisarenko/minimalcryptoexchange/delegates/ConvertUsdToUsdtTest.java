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

package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;

import java.math.BigInteger;

import static com.dpisarenko.minimalcryptoexchange.delegates.ConvertUsdToUsdt.USD_TO_USDT_CONVERSION_FACTOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConvertUsdToUsdtTest {
    @Test
    public void givenUsdAmount_whenExecute_thenReturnCorrectUsdtAmount() throws Exception {
        // Given
        final ConvertUsdToUsdt sut = new ConvertUsdToUsdt();
        final Double usdAmount = 4.39;
        final long usdtAmountLong = (long) (4.39 * USD_TO_USDT_CONVERSION_FACTOR);
        final BigInteger usdtAmount = BigInteger.valueOf(usdtAmountLong);

        final DelegateExecution delEx = mock(DelegateExecution.class);
        when(delEx.getVariable("USD_AMOUNT")).thenReturn(usdAmount);

        // When
        sut.execute(delEx);

        // Then
        verify(delEx).getVariable("USD_AMOUNT");
        verify(delEx).setVariable("USDT_AMOUNT", usdtAmount);

    }

}