package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.net.InetSocketAddress;


public class WalletObserver {
    @Autowired
    ClojureService clojureService;

    @Value("${accounts.btc.exchange.address}")
    String exchangeAddress;

    public void init() {
        BriefLogFormatter.init();
        final LocalTestNetParams netParams = new LocalTestNetParams();
        netParams.setPort(18444);

        //RegTestParams netParams = RegTestParams.get();

        try {
            //
            final WalletAppKit kit = new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
            kit.connectToLocalHost();
            kit.startAsync();
            kit.awaitRunning();

            System.out.println("Port works");

            //final Wallet wallet = Wallet.createBasic(netParams);

            // TODO: Try out the approach from here:
            // https://stackoverflow.com/questions/27727439/how-to-watch-for-transactions-for-an-address-in-bitcoinj-java?rq=1

            kit.wallet().addWatchedAddress(Address.fromString(netParams, "2N23tWAFEtBtTgxNjBNmnwzsiPdLcNek181"));

            kit.wallet().addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
                clojureService.btcTxReceived(wallet, tx, prevBalance, newBalance);
                // System.out.println("-----> coins resceived: " + tx.getTxId());
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
