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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellCommandExecutor {
    private final Logger logger;

    ShellCommandExecutor(Logger logger) {
        this.logger = logger;
    }

    public ShellCommandExecutor() {
        this(LoggerFactory.getLogger(ShellCommandExecutor.class));
    }

    public Outcome runShellCommand(final String command) {
        try {
            final Process process = Runtime.getRuntime().exec(command);
            final int exitValue = process.waitFor();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            final StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }

            if (exitValue == 0) {
                // TODO: Test this
                return new Outcome()
                        .withSuccess(true)
                        .withResult(sb.toString());
            }
            // TODO: Test this
            return new Outcome()
                    .withSuccess(false)
                    .withErrorMessage(sb.toString());
        } catch (final IOException | InterruptedException exception) {
            // TODO: Test this
            logger.error(String.format("Error ocurred while executing command '%s'", command), exception);
            return new Outcome()
                    .withSuccess(false)
                    .withErrorMessage(exception.getMessage());
        }
    }
}
