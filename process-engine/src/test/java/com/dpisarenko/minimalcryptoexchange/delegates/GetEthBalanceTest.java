package com.dpisarenko.minimalcryptoexchange.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.slf4j.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

public class GetEthBalanceTest {
    @Test
    public void givenAddress_whenExecute_thenSetProcessVariableToBalance() throws Exception {
        // Given
        final String ethNetworkUrl = "ethNetworkUrl";
        final String exchangeAddressEth = "exchangeAddressEth";
        final Logger logger = mock(Logger.class);
        final GetEthBalance sut = spy(new GetEthBalance(logger));
        sut.exchangeAddressEth = exchangeAddressEth;
        sut.ethNetworkUrl = ethNetworkUrl;
        final Web3j web3 = mock(Web3j.class);

        doReturn(web3).when(sut).createWeb3If(ethNetworkUrl);

        final BigInteger balanceWei = BigInteger.ONE;

        final EthGetBalance response = mock(EthGetBalance.class);
        when(response.getBalance()).thenReturn(balanceWei);

        final CompletableFuture<EthGetBalance> completableFuture =
                mock(CompletableFuture.class);
        when(completableFuture.get()).thenReturn(response);

        final Request<Object, EthGetBalance> request = mock(Request.class);
        when(request.sendAsync()).thenReturn(completableFuture);

        doReturn(request).when(web3)
                .ethGetBalance(exchangeAddressEth, LATEST);

        final DelegateExecution delEx = mock(DelegateExecution.class);

        // When
        sut.execute(delEx);

        // Then
        verify(sut).execute(delEx);
        verify(sut).createWeb3If(ethNetworkUrl);
        verify(web3).ethGetBalance(exchangeAddressEth, LATEST);
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