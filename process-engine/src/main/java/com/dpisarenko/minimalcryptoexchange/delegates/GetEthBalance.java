package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Component
public class GetEthBalance implements JavaDelegate {
    private final Logger logger;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddressEth;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    GetEthBalance(Logger logger) {
        this.logger = logger;
    }

    public GetEthBalance() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        logger.debug("Hello");
        final Web3j web3 = Web3j.build(new HttpService(ethNetworkUrl));

        final EthGetBalance response = web3.ethGetBalance(exchangeAddressEth, DefaultBlockParameterName.LATEST).sendAsync().get();
        final BigInteger balanceWei = response.getBalance();
        logger.debug("Balance in wei: " + balanceWei);
        delEx.setVariable("EXCHANGE_ACCOUNT_BALANCE_WEI",
                balanceWei);
    }
}
