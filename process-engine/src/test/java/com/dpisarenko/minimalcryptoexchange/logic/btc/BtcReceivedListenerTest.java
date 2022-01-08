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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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

        // When
        sut.onCoinsReceived(wallet, tx, prevBalance, newBalance);

        // Then
        //verify(clojureService).btcTxReceived();
    }

}