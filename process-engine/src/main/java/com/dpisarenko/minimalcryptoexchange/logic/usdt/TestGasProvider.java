package com.dpisarenko.minimalcryptoexchange.logic.usdt;

import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class TestGasProvider extends StaticGasProvider {
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(1L);
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(1L);

    public TestGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}
