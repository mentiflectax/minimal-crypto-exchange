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

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.slf4j.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.dpisarenko.minimalcryptoexchange.delegates.TestConstants.ETH_NETWORK_URL;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

public class GetEthBalanceTest {

    public static final String EXCHANGE_ADDRESS_ETH = "exchangeAddressEth";

    @Test
    public void givenAddress_whenExecute_thenSetProcessVariableToBalance() throws Exception {
        // Given
        final Logger logger = mock(Logger.class);
        final Function<String, Web3j> createWeb3j = mock(Function.class);
        final GetEthBalance sut = spy(new GetEthBalance(logger, createWeb3j));
        sut.exchangeAddressEth = EXCHANGE_ADDRESS_ETH;
        sut.ethNetworkUrl = ETH_NETWORK_URL;
        final Web3j web3 = mock(Web3j.class);

        when(createWeb3j.apply(ETH_NETWORK_URL)).thenReturn(web3);

        final BigInteger balanceWei = BigInteger.ONE;

        final EthGetBalance response = mock(EthGetBalance.class);
        when(response.getBalance()).thenReturn(balanceWei);

        final CompletableFuture<EthGetBalance> completableFuture =
                mock(CompletableFuture.class);
        when(completableFuture.get()).thenReturn(response);

        final Request<Object, EthGetBalance> request = mock(Request.class);
        when(request.sendAsync()).thenReturn(completableFuture);

        doReturn(request).when(web3)
                .ethGetBalance(EXCHANGE_ADDRESS_ETH, LATEST);

        final DelegateExecution delEx = mock(DelegateExecution.class);

        // When
        sut.execute(delEx);

        // Then
        verify(sut).execute(delEx);
        verify(createWeb3j).apply(ETH_NETWORK_URL);
        verify(web3).ethGetBalance(EXCHANGE_ADDRESS_ETH, LATEST);
        verify(request).sendAsync();
        verify(completableFuture).get();
        verify(response).getBalance();
        verify(logger).info("Balance of account 'exchangeAddressEth' is equal to 1 wei (network 'ethNetworkUrl')");
        verify(delEx).setVariable("EXCHANGE_ACCOUNT_BALANCE_WEI",
                balanceWei.longValue());
        verifyNoMoreInteractions(sut, web3, request,
                response, completableFuture, logger,
                delEx);
    }
}