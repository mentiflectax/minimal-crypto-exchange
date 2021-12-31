package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.EthUtils;
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

import static java.lang.String.format;

@Component("TransferUsdtToExchangeAccount")
public class TransferUsdtToExchangeAccount implements JavaDelegate {
    private final Logger logger;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddressEth;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    public TransferUsdtToExchangeAccount(Logger logger) {
        this.logger = logger;
    }

    public TransferUsdtToExchangeAccount() {
        this(LoggerFactory.getLogger(GetEthBalance.class));
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        final Web3j web3 = createWeb3If(ethNetworkUrl);
        final Credentials credentials = Credentials.create(privateKey);
        final BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();
        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider(gasPrice, gasPrice.multiply(BigInteger.valueOf(100))));

        // Check the balance
        final BigInteger oldBalance = EthUtils.getEthBalanceInWei(web3, exchangeAddress);

        // Send ETH to the exchange account

        // Send USDT to the exchange account
        logger.info("Starting to transfer USDT to the exchange address");
        try {
            usdtContract.transfer(exchangeAddress, BigInteger.valueOf(10)).send();
        } catch (final Exception exception) {
            logger.error("", exception);
        }
    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
