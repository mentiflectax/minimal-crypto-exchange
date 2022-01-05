package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.usdt.TestGasProvider;
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

import java.math.BigInteger;

@Component("SendEthDelegate")
public class SendEthDelegate implements JavaDelegate {
    private final Logger logger;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    SendEthDelegate(Logger logger) {
        this.logger = logger;
    }

    public SendEthDelegate() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }

    @Override
    public void execute(DelegateExecution delEx) throws Exception {
        // TODO: Test this
        final Web3j web3 = createWeb3If(ethNetworkUrl);

        final Credentials credentials = Credentials.create(privateKey);

        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider(BigInteger.valueOf(1), BigInteger.valueOf(2*Short.MAX_VALUE)));

        final String targetEthAddress = (String) delEx.getVariable("TARGET_ETH_ADDRESS");
        final BigInteger usdtAmount = (BigInteger) delEx.getVariable("USDT_AMOUNT");
        usdtContract.transferFrom(exchangeAddress, targetEthAddress, usdtAmount).send();
    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
