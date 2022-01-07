package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.CreateWeb3j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.function.Function;

import static java.lang.String.format;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

@Component("GetEthBalance")
public class GetEthBalance implements JavaDelegate {
    private final Logger logger;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddressEth;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    private final Function<String, Web3j> createWeb3j;

    GetEthBalance(Logger logger, Function<String, Web3j> createWeb3j) {
        this.logger = logger;
        this.createWeb3j = createWeb3j;
    }

    public GetEthBalance() {
        this(LoggerFactory.getLogger(GetEthBalance.class), new CreateWeb3j());
    }

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        final Web3j web3 = createWeb3j.apply(ethNetworkUrl);
        final EthGetBalance response = web3.ethGetBalance(exchangeAddressEth, LATEST).sendAsync().get();
        final BigInteger balanceWei = response.getBalance();
        logger.info(format("Balance of account '%s' is equal to %d wei (network '%s')",
                exchangeAddressEth, balanceWei.longValue(), ethNetworkUrl));
        delEx.setVariable("EXCHANGE_ACCOUNT_BALANCE_WEI",
                balanceWei.longValue());
    }
}
