package com.dpisarenko.minimalcryptoexchange.logic.moneyarrived;

import com.dpisarenko.minimalcryptoexchange.common.Currency;

public class IsMoneyArrivedInput {
    private Currency currency;

    public IsMoneyArrivedInput withCurrency(final Currency newCurrency) {
        currency = newCurrency;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }
}
