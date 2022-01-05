package com.dpisarenko.minimalcryptoexchange.logic.eth;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.function.Function;

public class CreateWeb3j implements Function<String, Web3j> {
    @Override
    public Web3j apply(String url) {
        return Web3j.build(new HttpService(url));
    }
}
