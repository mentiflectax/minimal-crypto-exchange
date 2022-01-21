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

package com.dpisarenko.minimalcryptoexchange.logic;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class ShellCommandExecutor {
    private final Logger logger;

    ShellCommandExecutor(Logger logger) {
        this.logger = logger;
    }

    public ShellCommandExecutor() {
        this(LoggerFactory.getLogger(ShellCommandExecutor.class));
    }

    public String runShellCommand(final String command) {
        try {
            final Process process = Runtime.getRuntime().exec(command);
            final int exitValue = process.waitFor();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            final StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            sb.append(line);
            while (line != null) {
                line = reader.readLine();
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            logger.error("", e);
            return null;
        } catch (InterruptedException e) {
            logger.error("", e);
            return null;
        }
    }

    public boolean runShellCommandOld(final String command) {
        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(1);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
        executor.setWatchdog(watchdog);
        try {
            int exitValue = executor.execute(cmdLine);
            return true;
        } catch (IOException e) {
            logger.error("", e);
        }
        return false;
    }
}
