package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.CreateWeb3j;
import org.junit.Test;
import org.web3j.protocol.Web3j;

import java.util.function.Function;

public class ApproveUsdtTransferTest {

    @Test
    public void givenDelegateExecution_whenExecute_thenApproveAmount() {
        // Given
        final Function<String, Web3j> createWeb3j = new CreateWeb3j();
        final ApproveUsdtTransfer sut = new ApproveUsdtTransfer(createWeb3j);
        sut.privateKey = "privateKey";
        sut.usdtContractAddress = "usdtContractAddress";
        sut.ethNetworkUrl = "ethNetworkUrl";
        sut.exchangeAddress = "exchangeAddress";

        // When

        // Then
    }
}