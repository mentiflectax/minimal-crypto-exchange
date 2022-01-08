package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.jetbrains.annotations.NotNull;
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
