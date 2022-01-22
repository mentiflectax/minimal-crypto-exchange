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
