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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.Web3j;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GetReceivedUsdtTest {
    @Test
    public void givenNoTransactionReceipts_whenExecute_thenThrowRuntimeException() throws Exception {
        // Given
        final Function<LoadErc20ContractInput, ERC20> loadErc20Contract = mock(Function.class);
        final Function<String, Web3j> createWeb3j = mock(Function.class);
        final GetReceivedUsdt sut = new GetReceivedUsdt(loadErc20Contract, createWeb3j);
        sut.privateKey = "privateKey";
        sut.usdtContractAddress = "usdtContractAddress";
        sut.ethNetworkUrl = "ethNetworkUrl";

        final ERC20 usdtContract = mock(ERC20.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock iom) throws Throwable {
                final LoadErc20ContractInput input = iom.getArgument(0);
                assertEquals("ethNetworkUrl", input.getEthNetworkUrl());
                assertEquals("privateKey", input.getPrivateKey());
                assertEquals("usdtContractAddress", input.getUsdtContractAddress());
                return usdtContract;
            }
        }).when(loadErc20Contract).apply(any());


        final DelegateExecution delEx = mock(DelegateExecution.class);

        // When
        sut.execute(delEx);

        // Then
        verify(loadErc20Contract).apply(any());
        verifyNoMoreInteractions(sut, createWeb3j, loadErc20Contract, usdtContract);

    }

}