package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.params.RegTestParams;

public class LocalTestNetParams extends RegTestParams {
    public LocalTestNetParams withPort(final int newPort) {
        this.port = newPort;
        return this;
    }

    public void setPort(final int newPort) {
        this.port = newPort;
    }
}
