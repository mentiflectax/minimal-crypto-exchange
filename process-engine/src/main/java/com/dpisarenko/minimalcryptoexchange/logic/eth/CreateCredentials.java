package com.dpisarenko.minimalcryptoexchange.logic.eth;

import org.web3j.crypto.Credentials;

import java.util.function.Function;

public class CreateCredentials implements Function<String, Credentials> {
    @Override
    public Credentials apply(final String privateKey) {
        return Credentials.create(privateKey);
    }
}
