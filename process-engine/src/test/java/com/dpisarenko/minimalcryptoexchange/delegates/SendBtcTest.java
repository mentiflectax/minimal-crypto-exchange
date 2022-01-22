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

import com.dpisarenko.minimalcryptoexchange.Outcome;
import com.dpisarenko.minimalcryptoexchange.logic.ShellCommandExecutor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SendBtcTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void givenSuccessfulExecutionOfShellCommand_whenExecute_thenSetSendBtcTxIdVariable() throws Exception {
        // Given
        final ShellCommandExecutor shellCommandExecutor = mock(ShellCommandExecutor.class);
        final SendBtc sut = new SendBtc(shellCommandExecutor);
        sut.sendBtcCliCommandPattern = "/usr/local/bin/docker exec minimal-crypto-exchange_node_1 bitcoin-cli sendtoaddress %s %.8f";

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("BTC_AMOUNT")).thenReturn(BigDecimal.valueOf(3.5));
        when(delEx.getVariable("TARGET_BTC_ADDRESS")).thenReturn("2NDjv4EUtXxKpfHCMuTmNg4miU9QDqy8vKs");
        when(shellCommandExecutor.runShellCommand(anyString())).thenReturn(
                new Outcome()
                        .withSuccess(true)
                        .withResult("btxTxId"));

        // When
        sut.execute(delEx);

        // Then
        verify(delEx).getVariable("BTC_AMOUNT");
        verify(delEx).getVariable("TARGET_BTC_ADDRESS");
        verify(shellCommandExecutor).runShellCommand("/usr/local/bin/docker exec minimal-crypto-exchange_node_1 bitcoin-cli sendtoaddress 2NDjv4EUtXxKpfHCMuTmNg4miU9QDqy8vKs 3.50000000");
        verify(delEx).setVariable("SEND_BTC_TX_ID", "btxTxId");
        verifyNoMoreInteractions(delEx, shellCommandExecutor);
    }

    @Test
    public void givenFailedExecutionOfShellCommand_whenExecute_thenThrowException() throws Exception {
        // Given
        final ShellCommandExecutor shellCommandExecutor = mock(ShellCommandExecutor.class);
        final SendBtc sut = new SendBtc(shellCommandExecutor);
        sut.sendBtcCliCommandPattern = "/usr/local/bin/docker exec minimal-crypto-exchange_node_1 bitcoin-cli sendtoaddress %s %.8f";

        final DelegateExecution delEx = mock(DelegateExecution.class);

        when(delEx.getVariable("BTC_AMOUNT")).thenReturn(BigDecimal.valueOf(3.5));
        when(delEx.getVariable("TARGET_BTC_ADDRESS")).thenReturn("2NDjv4EUtXxKpfHCMuTmNg4miU9QDqy8vKs");
        when(shellCommandExecutor.runShellCommand(anyString())).thenReturn(
                new Outcome()
                        .withSuccess(false)
                        .withErrorMessage("error message"));

        // Then
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Could not send 3.500000 BTC to '2NDjv4EUtXxKpfHCMuTmNg4miU9QDqy8vKs' using '/usr/local/bin/docker exec minimal-crypto-exchange_node_1 bitcoin-cli sendtoaddress 2NDjv4EUtXxKpfHCMuTmNg4miU9QDqy8vKs 3.50000000' (details: 'error message')");

        // When
        sut.execute(delEx);
    }
}