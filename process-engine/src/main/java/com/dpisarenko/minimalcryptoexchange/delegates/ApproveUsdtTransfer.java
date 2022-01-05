package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.CreateWeb3j;
import com.dpisarenko.minimalcryptoexchange.logic.usdt.TestGasProvider;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.function.Function;

@Component("ApproveUsdtTransfer")
public class ApproveUsdtTransfer implements JavaDelegate {
    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    private final Function<String,Web3j> createWeb3j;

    ApproveUsdtTransfer(Function<String, Web3j> createWeb3j) {
        this.createWeb3j = createWeb3j;
    }

    ApproveUsdtTransfer() {
        this(new CreateWeb3j());
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // TODO: Test this
        final Web3j web3 = createWeb3j.apply(ethNetworkUrl);
        final Credentials credentials = Credentials.create(privateKey);
        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider(BigInteger.valueOf(1), BigInteger.valueOf(2*Short.MAX_VALUE)));

        final BigInteger usdtAmount = (BigInteger) delegateExecution.getVariable("USDT_AMOUNT");
        final BigInteger amountToApprove = usdtAmount.add(BigInteger.ONE);
        usdtContract.approve(exchangeAddress, amountToApprove).send();
    }
}
