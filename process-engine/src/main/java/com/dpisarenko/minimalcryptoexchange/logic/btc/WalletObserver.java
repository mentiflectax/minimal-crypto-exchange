package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

public class WalletObserver {
    public void init() {
        final NetworkParameters netParams = NetworkParameters.fromID(NetworkParameters.ID_REGTEST);

        try {
            final Wallet wallet = Wallet.createBasic(netParams);

            wallet.addWatchedAddress(Address.fromString(netParams, "2N23tWAFEtBtTgxNjBNmnwzsiPdLcNek181"));

            wallet.addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
                @Override
                public void onCoinsReceived(final Wallet wallet, final Transaction transaction, final Coin prevBalance, final Coin newBalance) {
                    System.out.println("Heyo!");
                }
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
