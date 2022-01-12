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

package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;

public class LogicalTransactionOutput {
    private Address targetAddress;
    private Coin amount;

    public LogicalTransactionOutput() {
    }

    public LogicalTransactionOutput withTargetAddress(final Address newAddress) {
        this.targetAddress = newAddress;
        return this;
    }

    public LogicalTransactionOutput withAmount(final Coin newAmount) {
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
