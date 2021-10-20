package com.dpisarenko.minimalcryptoexchange.logic.moneyarrived;

import com.dpisarenko.minimalcryptoexchange.common.Currency;
import org.bitcoinj.core.NetworkParameters;
import org.slf4j.Logger;

public class IsMoneyArrivedInput {
    private Currency currency;
    private NetworkParameters networkParameters;
    private Logger logger;

    public IsMoneyArrivedInput withCurrency(final Currency newCurrency) {
        currency = newCurrency;
        return this;
    }

    public IsMoneyArrivedInput withnetworkParameters(final NetworkParameters newNetworkParameters) {
        networkParameters = newNetworkParameters;
        return this;
    }

    public IsMoneyArrivedInput withLogger(final Logger newLogger) {
        logger = newLogger;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public NetworkParameters getNetworkParameters() {
        return networkParameters;
    }

    public Logger getLogger() {
        return logger;
    }
}
