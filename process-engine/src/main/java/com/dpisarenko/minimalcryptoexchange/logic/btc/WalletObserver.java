package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Optional;

@Component
public class WalletObserver {
    @Autowired
    ClojureService clojureService;

    @Value("${accounts.btc.exchange.address}")
    String exchangeAddress;

    @PostConstruct
    public void init() {
        // TODO: Test this
        BriefLogFormatter.init();
        final LocalTestNetParams netParams = new LocalTestNetParams();
        netParams.setPort(18444);
        final WalletAppKit kit = new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
        kit.connectToLocalHost();
        kit.startAsync();
        kit.awaitRunning();
        try {
            kit.wallet().addWatchedAddress(Address.fromString(netParams,  exchangeAddress));
            // TODO: Put the listener into a separate class
            // TODO: Write an automated test for the listener
            kit.wallet().addCoinsReceivedEventListener(new BtcReceivedListener(clojureService, exchangeAddress, netParams));
        }
        catch (Exception exception) {
            // TODO: Test this
            // TODO: Log this error
            exception.printStackTrace();
        }
    }
}
