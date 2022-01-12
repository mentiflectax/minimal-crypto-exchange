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

package com.dpisarenko.minimalcryptoexchange.logic.eth;

import com.dpisarenko.minimalcryptoexchange.logic.usdt.TestGasProvider;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.function.Function;

public class LoadErc20Contract implements Function<LoadErc20ContractInput, ERC20> {
    public static final TestGasProvider GAS_PROVIDER = new TestGasProvider(BigInteger.valueOf(1), BigInteger.valueOf(2 * Short.MAX_VALUE));

    private final Function<String, Web3j> createWeb3j;

    private final Function<String, Credentials> createCredentials;

    public LoadErc20Contract(Function<String, Web3j> createWeb3j, Function<String, Credentials> createCredentials) {
        this.createWeb3j = createWeb3j;
        this.createCredentials = createCredentials;
    }

    public LoadErc20Contract() {
        this(new CreateWeb3j(),
                new CreateCredentials());
    }

    @Override
    public ERC20 apply(final LoadErc20ContractInput input) {
        final Web3j web3 = createWeb3j.apply(input.getEthNetworkUrl());
        final Credentials credentials = createCredentials.apply(input.getPrivateKey());
        return ERC20.load(input.getUsdtContractAddress(), web3, credentials, GAS_PROVIDER);
    }
}
