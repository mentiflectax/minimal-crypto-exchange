package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

import static java.lang.String.format;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

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
    public void execute(final DelegateExecution delEx) throws Exception {
        final Web3j web3 = createWeb3If(ethNetworkUrl);
        final EthGetBalance response = web3.ethGetBalance(exchangeAddressEth, LATEST).sendAsync().get();
        final BigInteger balanceWei = response.getBalance();
        logger.info(format("Balance of account '%s' is equal to %d wei (network '%s')",
                exchangeAddressEth, balanceWei.longValue(), ethNetworkUrl));
        delEx.setVariable("EXCHANGE_ACCOUNT_BALANCE_WEI",
                balanceWei);
    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
