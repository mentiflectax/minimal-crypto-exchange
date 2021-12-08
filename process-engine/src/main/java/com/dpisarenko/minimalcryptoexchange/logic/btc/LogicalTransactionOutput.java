package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;

class LogicalTransactionOutput {
    private Address targetAddress;
    private Coin amount;

    public LogicalTransactionOutput() {
    }

    LogicalTransactionOutput withTargetAddress(final Address newAddress) {
        this.targetAddress = newAddress;
        return this;
    }

    LogicalTransactionOutput withAmount(final Coin newAmount) {
        this.amount = newAmount;
        return this;
    }

    public Address getTargetAddress() {
        return targetAddress;
    }

    public Coin getAmount() {
        return amount;
    }
}
