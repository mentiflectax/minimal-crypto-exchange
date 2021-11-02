package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.params.RegTestParams;

public class LocalTestNetParams extends RegTestParams {
    public void setPort(final int newPort) {
        this.port = newPort;
    }
}
