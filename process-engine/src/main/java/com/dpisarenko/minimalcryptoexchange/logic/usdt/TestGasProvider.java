package com.dpisarenko.minimalcryptoexchange.logic.usdt;

import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class TestGasProvider extends StaticGasProvider {
    public static final BigInteger GAS_PRICE = new BigInteger("ffffff", 16);
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(1000000000L);

    public TestGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}
