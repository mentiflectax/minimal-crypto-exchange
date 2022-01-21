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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

public class ShellCommandExecutor {
    private final Logger logger;

    ShellCommandExecutor(Logger logger) {
        this.logger = logger;
    }

    public ShellCommandExecutor() {
        this(LoggerFactory.getLogger(ShellCommandExecutor.class));
    }

    public boolean runShellCommand(final String command) {
        final String osName = System.getProperty("os.name");
        final OperatingSystem os;
        if ("Mac OS X".equals(osName)) {
            os = OperatingSystem.MAC_OS_X;
        } else if (osName.toLowerCase().startsWith("windows")) {
            os = OperatingSystem.WINDOWS;
        } else {
            os = OperatingSystem.UNSUPPORTED;
        }

        if (OperatingSystem.UNSUPPORTED.equals(os)) {
            logger.error(String.format("Operating system '%s' is not supported", osName));
            return false;
        }

        final ProcessBuilder processBuilder = new ProcessBuilder();
        if (OperatingSystem.MAC_OS_X.equals(os)) {
            processBuilder.command("bash", "-c", command);
        } else if (OperatingSystem.WINDOWS.equals(os)) {
            processBuilder.command("cmd.exe", "/c", command);
        }

        try {

            // Process process = processBuilder.start();

            final String fullCommand = String.format("sh -c %s",
                    command);

            //Process process = Runtime.getRuntime().exec(fullCommand);

            Process process = Runtime.getRuntime().exec(new String[]{
                    "sh",
                    "docker exec -it minimal-crypto-exchange_node_1 bitcoin-cli sendtoaddress \"2NDjv4EUtXxKpfHCMuTmNg4miU9QDqy8vKs\" 0.1"
            });

            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitVal = process.waitFor();
            if (exitVal == 0) {

                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
