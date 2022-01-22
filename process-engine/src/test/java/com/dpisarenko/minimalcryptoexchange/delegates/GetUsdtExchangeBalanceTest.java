/*
 * Copyright 2021, 2022 Dmitrii Pisarenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.core.RemoteCall;

import java.math.BigInteger;
import java.util.function.Function;

import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.ETH_NETWORK_URL;
import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.EXCHANGE_ADDRESS;
import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.PRIVATE_KEY;
import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.USDT_CONTRACT_ADDRESS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetUsdtExchangeBalanceTest {
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