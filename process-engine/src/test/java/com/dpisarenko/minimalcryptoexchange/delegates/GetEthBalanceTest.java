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