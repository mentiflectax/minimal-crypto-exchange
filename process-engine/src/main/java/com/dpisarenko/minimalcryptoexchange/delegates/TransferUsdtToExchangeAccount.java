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
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

import static java.lang.String.format;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

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
        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider());

        // Send USDT to the exchange account
        logger.info("Starting to transfer USDT to the exchange address");
        try {
            usdtContract.transfer(exchangeAddress, BigInteger.valueOf(10)).send();
        } catch (final Exception exception) {
            logger.error("", exception);
        }

        // Check the balance
        final EthGetBalance getBalanceResponse = web3.ethGetBalance(exchangeAddressEth, LATEST).sendAsync().get();
        final BigInteger balanceWei = getBalanceResponse.getBalance();
        logger.info(format("Balance of account '%s' is equal to %d wei (network '%s')",
                exchangeAddressEth, balanceWei.longValue(), ethNetworkUrl));

    }

    Web3j createWeb3If(final String url) {
        return Web3j.build(new HttpService(url));
    }
}
