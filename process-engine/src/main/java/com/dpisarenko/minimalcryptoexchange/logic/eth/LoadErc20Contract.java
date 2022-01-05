package com.dpisarenko.minimalcryptoexchange.logic.eth;

import com.dpisarenko.minimalcryptoexchange.logic.usdt.TestGasProvider;
import org.web3j.contracts.eip20.generated.ERC20;

import java.math.BigInteger;
import java.util.function.Function;

public class LoadErc20Contract implements Function<LoadErc20ContractInput, ERC20> {



    @Override
    public ERC20 apply(final LoadErc20ContractInput input) {

        return ERC20.load(usdtContractAddress, web3, credentials, new TestGasProvider(BigInteger.valueOf(1), BigInteger.valueOf(2 * Short.MAX_VALUE)));
    }
}
