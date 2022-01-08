package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20Contract;
import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.contracts.eip20.generated.ERC20;

import java.math.BigInteger;
import java.util.function.Function;

@Component("SendEthDelegate")
public class SendEthDelegate implements JavaDelegate {
    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    private final Function<LoadErc20ContractInput, ERC20> loadErc20Contract;

    SendEthDelegate(final Function<LoadErc20ContractInput, ERC20> loadErc20Contract) {
        this.loadErc20Contract = loadErc20Contract;
    }

    public SendEthDelegate() {
        this(new LoadErc20Contract());
    }

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        // TODO: Test this
        final ERC20 usdtContract = loadErc20Contract.apply(
                new LoadErc20ContractInput()
                        .withPrivateKey(privateKey)
                        .withUsdtContractAddress(usdtContractAddress)
                        .withEthNetworkUrl(ethNetworkUrl));

        final String targetEthAddress = (String) delEx.getVariable("TARGET_ETH_ADDRESS");
        final BigInteger usdtAmount = (BigInteger) delEx.getVariable("USDT_AMOUNT");
        usdtContract.transferFrom(exchangeAddress, targetEthAddress, usdtAmount).send();
    }
}
