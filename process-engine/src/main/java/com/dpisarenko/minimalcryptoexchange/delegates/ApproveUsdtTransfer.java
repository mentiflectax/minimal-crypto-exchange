package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.CreateCredentials;
import com.dpisarenko.minimalcryptoexchange.logic.eth.CreateWeb3j;
import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20Contract;
import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import com.dpisarenko.minimalcryptoexchange.logic.usdt.TestGasProvider;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

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

    private final Function<LoadErc20ContractInput, ERC20> loadErc20Contract;

    ApproveUsdtTransfer(Function<String, Web3j> createWeb3j, Function<String, Credentials> createCredentials, Function<LoadErc20ContractInput, ERC20> loadErc20Contract) {
        this.loadErc20Contract = loadErc20Contract;
    }

    ApproveUsdtTransfer() {
        this(new CreateWeb3j(),
                new CreateCredentials(), new LoadErc20Contract());
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // TODO: Test this
        final ERC20 usdtContract = loadErc20Contract.apply(new LoadErc20ContractInput()
                .withPrivateKey(privateKey)
                .withUsdtContractAddress(usdtContractAddress)
                .withEthNetworkUrl(ethNetworkUrl));
        final BigInteger usdtAmount = (BigInteger) delegateExecution.getVariable("USDT_AMOUNT");
        final BigInteger amountToApprove = usdtAmount.add(BigInteger.ONE);
        usdtContract.approve(exchangeAddress, amountToApprove).send();
    }
}
