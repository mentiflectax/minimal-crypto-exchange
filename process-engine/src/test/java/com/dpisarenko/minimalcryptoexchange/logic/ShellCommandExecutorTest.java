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

package com.dpisarenko.minimalcryptoexchange.logic;

import com.dpisarenko.minimalcryptoexchange.Outcome;
import kotlin.text.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ShellCommandExecutorTest {
    private static final String COMMAND = "/usr/local/bin/docker exec minimal-crypto-exchange_node_1 bitcoin-cli sendtoaddress 2N1akcbAa3oXJ7RNxrAWmL7ZuuU9iHg9tpG 0.01";
    private static final String TX_ID = "783bfb58b746f89fc72a7eac7ee49d22e8dd3741892f9316baffd8fec9d58c49";

    @Test
    public void givenSuccessfulExecutionOfCommandLineCommand_whenRunShellCommand_thenReturnSuccessValue() throws IOException, InterruptedException {
        // Given
        final Logger logger = mock(Logger.class);
        final ShellCommandExecutor sut = spy(new ShellCommandExecutor(logger));
        final Process process = mock(Process.class);
        doReturn(process).when(sut).exec(COMMAND);
        when(process.waitFor()).thenReturn(0);

        final InputStream inputStream = IOUtils.toInputStream(TX_ID, Charsets.UTF_8);
        when(process.getInputStream()).thenReturn(inputStream);

        // When
        final Outcome actualResult = sut.runShellCommand(COMMAND);

        // Then
        assertTrue(actualResult.isSuccess());
        assertEquals(TX_ID, actualResult.getResult());
    }
}