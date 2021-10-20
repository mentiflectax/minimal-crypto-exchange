package com.dpisarenko.minimalcryptoexchange.logic.moneyarrived;

import com.dpisarenko.minimalcryptoexchange.common.Currency;

import java.util.function.Function;

public class IsMoneyArrived implements Function<IsMoneyArrivedInput, IsMoneyArrivedResult> {
    @Override
    public IsMoneyArrivedResult apply(final IsMoneyArrivedInput input) {
        if (!Currency.BTC.equals(input.getCurrency())) {
            throw new IllegalArgumentException("Currently only BTC is supported");
        }

        return new IsMoneyArrivedResult();
    }
}
