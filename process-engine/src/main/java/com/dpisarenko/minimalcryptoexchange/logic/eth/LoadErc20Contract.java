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
