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
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("SendBtc")
public class SendBtc implements JavaDelegate {
    @Value("${btc.send-btc-cli-command-pattern}")
    String sendBtcCliCommandPattern;

    private final ShellCommandExecutor shellCommandExecutor;

    SendBtc(ShellCommandExecutor shellCommandExecutor) {
        this.shellCommandExecutor = shellCommandExecutor;
    }

    public SendBtc() {
        this(new ShellCommandExecutor());
    }

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        final BigDecimal btcAmount = (BigDecimal) delEx.getVariable("BTC_AMOUNT");
        final String targetBtcAddress = (String) delEx.getVariable("TARGET_BTC_ADDRESS");

        final String command = String.format(sendBtcCliCommandPattern, targetBtcAddress, btcAmount.doubleValue());

        final Outcome outcome = shellCommandExecutor.runShellCommand(command);
        if (outcome.isSuccess()) {
            delEx.setVariable("SEND_BTC_TX_ID", outcome.getResult());
        } else {
            throw new RuntimeException(String.format("Could not send %f BTC to '%s' using '%s' (details: '%s')", btcAmount.doubleValue(),
                    targetBtcAddress, command, outcome.getErrorMessage()));
        }
    }
}
