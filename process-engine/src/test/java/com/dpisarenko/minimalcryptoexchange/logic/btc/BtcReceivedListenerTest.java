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
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class BtcReceivedListenerTest {
    private static final String EXCHANGE_ADDRESS =  "2N1akcbAa3oXJ7RNxrAWmL7ZuuU9iHg9tpG";

    @Test
    public void givenIncomingBtcTxWithRightOutputs_whenOnCoinsReceived_thenCallBtcTxReceivedMethod() {
        // Given
        final ClojureService clojureService = mock(ClojureService.class);
        final NetworkParameters netParams = new LocalTestNetParams();
        final Logger logger = mock(Logger.class);
        final BtcReceivedListener sut = spy(new BtcReceivedListener(clojureService, EXCHANGE_ADDRESS, netParams, logger));

        final Wallet wallet = mock(Wallet.class);
        final Transaction tx = mock(Transaction.class);
        final Sha256Hash txId = Sha256Hash.of("txId".getBytes());
        when(tx.getTxId()).thenReturn(txId);
        final Coin prevBalance = Coin.ZERO;
        final Coin newBalance = Coin.SATOSHI;

        final LogicalTransactionOutput lto = mock(LogicalTransactionOutput.class);
        when(lto.getAmount()).thenReturn(newBalance);
        final Optional<LogicalTransactionOutput> relevantTxOutput = Optional.of(lto);
        doReturn(relevantTxOutput).when(sut).findRelevantTxOutput(tx);

        // When
        sut.onCoinsReceived(wallet, tx, prevBalance, newBalance);

        // Then
        verify(sut).onCoinsReceived(wallet, tx, prevBalance, newBalance);
        verify(logger).debug("Incoming BTC transaction registered: f9f1a1baf8cd977b0fb9534e593dc52f8e788c17e1d2541d51911d842fbca6af");
        verify(sut).findRelevantTxOutput(tx);
        verify(clojureService).btcTxReceived(txId.toString(), newBalance);
        verify(logger).info("Received 0.00000001 BTC");
        verifyNoMoreInteractions(sut, clojureService, logger);
    }
    @Test
    public void givenIncomingBtcTxWithoutRightOutputs_whenOnCoinsReceived_thenLogError() {
        // Given
        final ClojureService clojureService = mock(ClojureService.class);
        final NetworkParameters netParams = new LocalTestNetParams();
        final Logger logger = mock(Logger.class);
        final BtcReceivedListener sut = spy(new BtcReceivedListener(clojureService, EXCHANGE_ADDRESS, netParams, logger));

        final Wallet wallet = mock(Wallet.class);
        final Transaction tx = mock(Transaction.class);
        final Sha256Hash txId = Sha256Hash.of("txId".getBytes());
        when(tx.getTxId()).thenReturn(txId);
        final Coin prevBalance = Coin.ZERO;
        final Coin newBalance = Coin.SATOSHI;

        final Optional<LogicalTransactionOutput> relevantTxOutput = Optional.empty();
        doReturn(relevantTxOutput).when(sut).findRelevantTxOutput(tx);

        // When
        sut.onCoinsReceived(wallet, tx, prevBalance, newBalance);

        // Then
        verify(sut).onCoinsReceived(wallet, tx, prevBalance, newBalance);
        verify(logger).debug("Incoming BTC transaction registered: f9f1a1baf8cd977b0fb9534e593dc52f8e788c17e1d2541d51911d842fbca6af");
        verify(sut).findRelevantTxOutput(tx);
        verify(clojureService, never()).btcTxReceived(txId.toString(), newBalance);
        verify(logger).error("Error process incoming BTC transaction 'f9f1a1baf8cd977b0fb9534e593dc52f8e788c17e1d2541d51911d842fbca6af': Could not find correct TX output");
        verifyNoMoreInteractions(sut, clojureService, logger);
    }
}