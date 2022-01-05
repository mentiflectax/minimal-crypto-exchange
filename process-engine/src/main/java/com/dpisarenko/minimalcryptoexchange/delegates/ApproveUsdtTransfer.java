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

    private final Function<String, Web3j> createWeb3j;
    private final Function<String, Credentials> createCredentials;
    private final Function<LoadErc20ContractInput, ERC20> loadErc20Contract;

    ApproveUsdtTransfer(Function<String, Web3j> createWeb3j, Function<String, Credentials> createCredentials, Function<LoadErc20ContractInput, ERC20> loadErc20Contract) {
        this.createWeb3j = createWeb3j;
        this.createCredentials = createCredentials;
        this.loadErc20Contract = loadErc20Contract;
    }

    ApproveUsdtTransfer() {
        this(new CreateWeb3j(),
                new CreateCredentials(), new LoadErc20Contract());
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // TODO: Test this
        final Web3j web3 = createWeb3j.apply(ethNetworkUrl);
        final Credentials credentials = createCredentials.apply(privateKey);
        final ERC20 usdtContract = ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider(BigInteger.valueOf(1), BigInteger.valueOf(2 * Short.MAX_VALUE)));

        final BigInteger usdtAmount = (BigInteger) delegateExecution.getVariable("USDT_AMOUNT");
        final BigInteger amountToApprove = usdtAmount.add(BigInteger.ONE);
        usdtContract.approve(exchangeAddress, amountToApprove).send();
    }
}
