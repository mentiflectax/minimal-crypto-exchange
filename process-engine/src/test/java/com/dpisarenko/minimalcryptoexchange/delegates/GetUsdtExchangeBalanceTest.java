package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.core.RemoteCall;

import java.math.BigInteger;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetUsdtExchangeBalanceTest {
    private static final String ETH_NETWORK_URL = "ethNetworkUrl";
    private static final String USDT_CONTRACT_ADDRESS = "usdtContractAddress";
    private static final String EXCHANGE_ADDRESS = "exchangeAddress";
    private static final String PRIVATE_KEY = "privateKey";

    @Test
    public void givenUsdtContract_whenExecute_thenSetProcessVariableToExchangeUsdtBalance() throws Exception {
        // Given
        final Function<LoadErc20ContractInput, ERC20> loadErc20Contract = mock(Function.class);
        final GetUsdtExchangeBalance sut = new GetUsdtExchangeBalance(loadErc20Contract);
        sut.ethNetworkUrl = ETH_NETWORK_URL;
        sut.usdtContractAddress = USDT_CONTRACT_ADDRESS;
        sut.exchangeAddress = EXCHANGE_ADDRESS;
        sut.privateKey = PRIVATE_KEY;

        final DelegateExecution delEx = mock(DelegateExecution.class);

        final ERC20 usdtContract = mock(ERC20.class);
        when(loadErc20Contract.apply(new LoadErc20ContractInput()
                .withPrivateKey(PRIVATE_KEY)
                .withUsdtContractAddress(USDT_CONTRACT_ADDRESS)
                .withEthNetworkUrl(ETH_NETWORK_URL))).thenReturn(usdtContract);
        final RemoteCall<BigInteger> balanceOfResponse = mock(RemoteCall.class);
        when(usdtContract.balanceOf(EXCHANGE_ADDRESS)).thenReturn(balanceOfResponse);

        final BigInteger usdtBalance = BigInteger.valueOf(42);

        when(balanceOfResponse.send()).thenReturn(usdtBalance);

        // When
        sut.execute(delEx);

        // Then
        verify(loadErc20Contract).apply(new LoadErc20ContractInput()
                .withPrivateKey(PRIVATE_KEY)
                .withUsdtContractAddress(USDT_CONTRACT_ADDRESS)
                .withEthNetworkUrl(ETH_NETWORK_URL));
        verify(usdtContract).balanceOf(EXCHANGE_ADDRESS);
        verify(balanceOfResponse).send();
        verify(delEx).setVariable("USDT_EXCHANGE_BALANCE", usdtBalance.longValue());

    }
}