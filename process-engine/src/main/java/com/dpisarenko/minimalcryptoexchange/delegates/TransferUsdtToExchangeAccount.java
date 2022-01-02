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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

import static java.lang.String.format;
import static java.math.BigInteger.ONE;

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

    @Value("${accounts.eth.usdt.buffer-address}")
    String bufferAddress;

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
        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider(BigInteger.valueOf(1), BigInteger.valueOf(2*Short.MAX_VALUE)));

        // Check the balance
        final BigInteger oldBalance = EthUtils.getEthBalanceInWei(web3, exchangeAddress);

        // Send ETH to the exchange account

        // Send USDT to the exchange account
        logger.info("Starting to transfer USDT to the exchange address");
        // TODO: Is there an account which has any USDT?

        final BigInteger amount = BigInteger.valueOf(1);
        try {
            final TransactionReceipt approveResponse1 = usdtContract.approve(bufferAddress, BigInteger.valueOf(100000))
                    .send();
            logger.debug("sendResponse: " + approveResponse1);

            final TransactionReceipt approveResponse2 = usdtContract.approve(exchangeAddress, BigInteger.valueOf(100000))
                    .send();
            logger.debug("sendResponse: " + approveResponse2);

            // final TransactionReceipt sendResponse = usdtContract.transfer(exchangeAddress, amount).send();
            // logger.debug("sendResponse: " + sendResponse);

            final TransactionReceipt transferResponse = usdtContract.transferFrom(bufferAddress, exchangeAddress, amount).send();
            logger.debug("" + transferResponse);
            // final TransactionReceipt transferResponse = usdtContract.transfer(exchangeAddress, BigInteger.valueOf(1)).send();
        } catch (final Exception exception) {
            logger.error("", exception);
        }
    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
