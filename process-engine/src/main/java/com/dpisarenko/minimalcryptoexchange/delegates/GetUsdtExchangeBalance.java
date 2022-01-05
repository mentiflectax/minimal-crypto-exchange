package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@Component("GetUsdtExchangeBalance")
public class GetUsdtExchangeBalance implements JavaDelegate {
    private final Logger logger;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    GetUsdtExchangeBalance(Logger logger) {
        this.logger = logger;
    }

    public GetUsdtExchangeBalance() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        // TODO: Test this
        final Web3j web3 = createWeb3If(ethNetworkUrl);
        final Credentials credentials = Credentials.create(privateKey);
        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new DefaultGasProvider());

        final BigInteger usdtBalance = usdtContract.balanceOf(exchangeAddress).send();
        delEx.setVariable("USDT_EXCHANGE_BALANCE", usdtBalance.longValue());
    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
