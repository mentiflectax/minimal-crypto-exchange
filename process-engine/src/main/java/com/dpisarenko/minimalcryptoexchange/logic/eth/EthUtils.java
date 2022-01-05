package com.dpisarenko.minimalcryptoexchange.logic.eth;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

public final class EthUtils {
    private EthUtils() {

    }

    public static BigInteger getEthBalanceInWei(Web3j web3, final String address) throws ExecutionException, InterruptedException {
        // TODO: Test this
        final EthGetBalance getBalanceResponse = web3.ethGetBalance(address, LATEST).sendAsync().get();
        return getBalanceResponse.getBalance();
    }
}
