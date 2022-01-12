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

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BtcReceivedListener implements WalletCoinsReceivedEventListener {
    private final ClojureService clojureService;
    private final String exchangeAddress;
    private final NetworkParameters netParams;
    private final Logger logger;

    public BtcReceivedListener(final ClojureService clojureService, final String exchangeAddress, NetworkParameters netParams, final Logger logger) {
        this.clojureService = clojureService;
        this.exchangeAddress = exchangeAddress;
        this.netParams = netParams;
        this.logger = logger;
    }

    public BtcReceivedListener(final ClojureService clojureService, final String exchangeAddress, NetworkParameters netParams) {
        this(clojureService, exchangeAddress, netParams, LoggerFactory.getLogger(BtcReceivedListener.class));
    }

    @Override
    public void onCoinsReceived(final Wallet wallet, final Transaction tx, final Coin prevBalance, final Coin newBalance) {
        logger.debug(String.format("Incoming BTC transaction registered: %s", tx.getTxId().toString()));
        final Optional<LogicalTransactionOutput> relevantTxOutput = findRelevantTxOutput(tx);
        if (relevantTxOutput.isPresent()) {
            final String txId = tx.getTxId().toString();
            Coin amount = relevantTxOutput.get().getAmount();
            logger.info(String.format("Received %s", amount.toFriendlyString()));
            clojureService.btcTxReceived(txId, amount);
        } else {
            logger.error(String.format("Error process incoming BTC transaction '%s': Could not find correct TX output", tx.getTxId().toString()));
        }
    }

    Optional<LogicalTransactionOutput> findRelevantTxOutput(Transaction tx) {
        return tx.getOutputs()
                .stream()
                .map(this::createLogicalTxOutput)
                .filter(logicalTransactionOutput -> exchangeAddress.equals(logicalTransactionOutput.getTargetAddress().toString()))
                .findFirst();
    }

    LogicalTransactionOutput createLogicalTxOutput(TransactionOutput output) {
        return new LogicalTransactionOutput()
                .withTargetAddress(output.getScriptPubKey().getToAddress(netParams))
                .withAmount(output.getValue());
    }
}
