package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class WalletObserver {
    @Autowired
    ClojureService clojureService;

    @Value("${accounts.btc.exchange.address}")
    String exchangeAddress;

    @PostConstruct
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
