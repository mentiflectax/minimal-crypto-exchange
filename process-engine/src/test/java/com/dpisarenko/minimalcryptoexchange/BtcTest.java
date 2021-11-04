package com.dpisarenko.minimalcryptoexchange;

import org.btc4j.core.BtcException;
import org.btc4j.core.BtcInfo;
import org.btc4j.daemon.BtcDaemon;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class BtcTest {
    @Test
    public void manualTest() throws MalformedURLException, BtcException {
        BtcDaemon daemon = new BtcDaemon(new URL("http://127.0.0.1:19000"));
        BtcInfo info = daemon.getInformation();
        System.out.println("Hello");
    }
}
