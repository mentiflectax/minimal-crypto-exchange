package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component("SendEthDelegate")
public class SendEthDelegate implements JavaDelegate {
    private final Logger logger;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    SendEthDelegate(Logger logger) {
        this.logger = logger;
    }

    public SendEthDelegate() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        final Web3j web3 = createWeb3If(ethNetworkUrl);
        logger.info("Hello");


        //web3.
    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
