/*
 * Copyright 2021, 2022 Dmitrii Pisarenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

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