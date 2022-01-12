package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mockito;
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

public class ApproveUsdtTransferTest {
    @Test
    public void givenDelegateExecution_whenExecute_thenApproveAmount() throws Exception {
        // Given
        final Function<LoadErc20ContractInput, ERC20> loadErc20Contract = mock(Function.class);
        final ApproveUsdtTransfer sut = new ApproveUsdtTransfer(loadErc20Contract);
        sut.privateKey = PRIVATE_KEY;
        sut.usdtContractAddress = USDT_CONTRACT_ADDRESS;
        sut.ethNetworkUrl = ETH_NETWORK_URL;
        sut.exchangeAddress = EXCHANGE_ADDRESS;

        final DelegateExecution delEx = mock(DelegateExecution.class);

        final ERC20 usdtContract = mock(ERC20.class);

        when(loadErc20Contract.apply(Mockito.eq(new LoadErc20ContractInput()
                .withPrivateKey(PRIVATE_KEY)
                .withUsdtContractAddress(USDT_CONTRACT_ADDRESS)
                .withEthNetworkUrl(ETH_NETWORK_URL)))).thenReturn(usdtContract);

        final BigInteger usdtAmount = BigInteger.ONE;
        when(delEx.getVariable("USDT_AMOUNT")).thenReturn(usdtAmount);

        final BigInteger amountToApprove = BigInteger.valueOf(2);

        final RemoteCall<TransactionReceipt> approveResult = mock(RemoteCall.class);

        when(usdtContract.approve(EXCHANGE_ADDRESS, amountToApprove)).thenReturn(approveResult);

        // When
        sut.execute(delEx);

        // Then
        verify(loadErc20Contract).apply(Mockito.eq(new LoadErc20ContractInput()
                .withPrivateKey(PRIVATE_KEY)
                .withUsdtContractAddress(USDT_CONTRACT_ADDRESS)
                .withEthNetworkUrl(ETH_NETWORK_URL)));
        verify(usdtContract).approve(EXCHANGE_ADDRESS, amountToApprove);
        verify(approveResult).send();
    }
}