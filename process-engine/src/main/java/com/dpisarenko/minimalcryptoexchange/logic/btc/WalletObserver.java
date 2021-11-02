package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import java.io.File;
import java.net.InetSocketAddress;

public class WalletObserver {
    public void init() {
        final LocalTestNetParams netParams = new LocalTestNetParams();
        netParams.setPort(19001);
        try {
            //
            final WalletAppKit kit = new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
            kit.setAutoSave(true);
            kit.peerGroup().connectTo(new InetSocketAddress("127.0.0.1", 19001));
            kit.connectToLocalHost();

            kit.startAsync();
            kit.awaitRunning();
            //kit.peerGroup().addPeerDiscovery(new DnsDiscovery(netParams));


            //final Wallet wallet = Wallet.createBasic(netParams);

            // TODO: Try out the approach from here:
            // https://stackoverflow.com/questions/27727439/how-to-watch-for-transactions-for-an-address-in-bitcoinj-java?rq=1

            kit.wallet().addWatchedAddress(Address.fromString(netParams, "2N23tWAFEtBtTgxNjBNmnwzsiPdLcNek181"));

            kit.wallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
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
