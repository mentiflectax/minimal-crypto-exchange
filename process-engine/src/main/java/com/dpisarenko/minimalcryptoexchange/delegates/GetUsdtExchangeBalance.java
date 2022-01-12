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

@Component("GetUsdtExchangeBalance")
public class GetUsdtExchangeBalance implements JavaDelegate {
    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    private final Function<LoadErc20ContractInput, ERC20> loadErc20Contract;

    GetUsdtExchangeBalance(final Function<LoadErc20ContractInput, ERC20> loadErc20Contract) {
        this.loadErc20Contract = loadErc20Contract;
    }

    public GetUsdtExchangeBalance() {
        this(new LoadErc20Contract());
    }

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        final ERC20 usdtContract = loadErc20Contract.apply(
                new LoadErc20ContractInput()
                        .withPrivateKey(privateKey)
                        .withUsdtContractAddress(usdtContractAddress)
                        .withEthNetworkUrl(ethNetworkUrl));

        final BigInteger usdtBalance = usdtContract.balanceOf(exchangeAddress).send();
        delEx.setVariable("USDT_EXCHANGE_BALANCE", usdtBalance.longValue());
    }
}
