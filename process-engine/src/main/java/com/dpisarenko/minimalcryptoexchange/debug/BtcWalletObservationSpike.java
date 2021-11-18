package com.dpisarenko.minimalcryptoexchange.debug;

import com.dpisarenko.minimalcryptoexchange.logic.btc.LocalTestNetParams;
import com.dpisarenko.minimalcryptoexchange.logic.btc.WalletObserver;
import org.bitcoinj.core.Address;
import org.bitcoinj.kits.WalletAppKit;

import java.io.File;

public class BtcWalletObservationSpike {
    public static void main(final String[] args) {
        final BtcWalletObservationSpike app = new BtcWalletObservationSpike();
        app.run();
    }

    private synchronized void run() {
        final LocalTestNetParams params = new LocalTestNetParams();
        params.setPort(18444/*WalletObserver.CUR_PORT*/);
        WalletAppKit kit = new WalletAppKit(params, new File("."), "walletappkit-example");
        kit.connectToLocalHost();

        kit.startAsync();
        kit.awaitRunning();

        kit.wallet().addWatchedAddress(Address.fromString(params, "2NFUwJcEWZvB77MUzSTD12ea5R6Mbw9dmtD"));

        kit.wallet().addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
            System.out.println("-----> coins resceived: " + tx.getTxId());
        });

        while (true) {
            try {
                this.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
