package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.function.Function;

import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.ETH_NETWORK_URL;
import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.EXCHANGE_ADDRESS;
import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.PRIVATE_KEY;
import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.USDT_CONTRACT_ADDRESS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendEthDelegateTest {
    static final String TARGET_ETH_ADDRESS = "targetEthAddress";
    static final BigInteger USDT_AMOUNT = BigInteger.valueOf(42);

    @Test
    public void givenTargetEthAddressAndAmount_whenExecute_thenSendUsdtToTargetAddress() throws Exception {
        // Given
        final Function<LoadErc20ContractInput, ERC20> loadErc20Contract = mock(Function.class);
        final SendEthDelegate sut = new SendEthDelegate(loadErc20Contract);
        sut.ethNetworkUrl = ETH_NETWORK_URL;
        sut.usdtContractAddress = USDT_CONTRACT_ADDRESS;
        sut.exchangeAddress = EXCHANGE_ADDRESS;
        sut.privateKey = PRIVATE_KEY;

        final ERC20 usdtContract = mock(ERC20.class);
        when(loadErc20Contract.apply(new LoadErc20ContractInput()
                .withPrivateKey(PRIVATE_KEY)
                .withUsdtContractAddress(USDT_CONTRACT_ADDRESS)
                .withEthNetworkUrl(ETH_NETWORK_URL))).thenReturn(usdtContract);

        final DelegateExecution delEx = mock(DelegateExecution.class);
        when(delEx.getVariable("TARGET_ETH_ADDRESS")).thenReturn(TARGET_ETH_ADDRESS);
        when(delEx.getVariable("USDT_AMOUNT")).thenReturn(USDT_AMOUNT);

        final RemoteCall<TransactionReceipt> transferFromResult = mock(RemoteCall.class);

        when(usdtContract.transferFrom(EXCHANGE_ADDRESS, TARGET_ETH_ADDRESS, USDT_AMOUNT)).thenReturn(transferFromResult);

        // When
        sut.execute(delEx);

        // Then
        verify(loadErc20Contract).apply(new LoadErc20ContractInput()
                .withPrivateKey(PRIVATE_KEY)
                .withUsdtContractAddress(USDT_CONTRACT_ADDRESS)
                .withEthNetworkUrl(ETH_NETWORK_URL));
        verify(usdtContract).transferFrom(EXCHANGE_ADDRESS, TARGET_ETH_ADDRESS, USDT_AMOUNT);
        verify(transferFromResult).send();
    }
}