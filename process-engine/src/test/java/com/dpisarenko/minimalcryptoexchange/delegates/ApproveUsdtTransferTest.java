package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.junit.Test;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.util.function.Function;

import static org.mockito.Mockito.mock;

public class ApproveUsdtTransferTest {

    @Test
    public void givenDelegateExecution_whenExecute_thenApproveAmount() {
        // Given
        final Function<String, Web3j> createWeb3j = mock(Function.class);
        final Function<String, Credentials> createCredentials = mock(Function.class);
        final Function<LoadErc20ContractInput, ERC20> loadErc20Contract = mock(Function.class);
        final ApproveUsdtTransfer sut = new ApproveUsdtTransfer(loadErc20Contract);
        sut.privateKey = "privateKey";
        sut.usdtContractAddress = "usdtContractAddress";
        sut.ethNetworkUrl = "ethNetworkUrl";
        sut.exchangeAddress = "exchangeAddress";

        // When

        // Then
    }
}