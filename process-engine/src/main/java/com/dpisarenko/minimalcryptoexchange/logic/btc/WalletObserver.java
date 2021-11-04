package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import java.io.File;
import java.net.InetSocketAddress;

public class WalletObserver {
    final Integer[] POTENTIAL_PORTS = {
            19000,
            19001,
            28332,
            50001,
            50002,
            51001,
            51002
    };

    final Integer CUR_PORT = POTENTIAL_PORTS[6];

    public void init() {
        BriefLogFormatter.init();
        final LocalTestNetParams netParams = new LocalTestNetParams();
        netParams.setPort(CUR_PORT);
        try {
            //
            final WalletAppKit kit = new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
            kit.connectToLocalHost();
            kit.setAutoSave(false);
            kit.startAsync();
            kit.awaitRunning();

            System.out.println("Port works");


            kit.peerGroup().addPeerDiscovery(new DnsDiscovery(netParams));


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

    private void testConn(WalletAppKit kit, int port) {
        try {
            kit.peerGroup().connectTo(new InetSocketAddress("127.0.0.1", port));
            System.out.println("Port " + port + " works");
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
