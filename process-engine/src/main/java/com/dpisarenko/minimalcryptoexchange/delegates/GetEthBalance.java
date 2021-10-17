package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

public class GetEthBalance implements JavaDelegate {
    private final Logger logger;

    GetEthBalance(Logger logger) {
        this.logger = logger;
    }

    public GetEthBalance() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        final Web3j web3 = Web3j.build(new HttpService("http://localhost:8178"));

        final EthGetBalance response = web3.ethGetBalance("0x190FD61ED8fE0067f0f09EA992C1BF96209bab66", DefaultBlockParameterName.LATEST).sendAsync().get();
        final BigInteger balanceWei = response.getBalance();
        logger.debug("Balance in wei: " + balanceWei);
        delEx.setVariable("EXCHANGE_ACCOUNT_BALANCE_WEI",
                balanceWei);
    }
}
