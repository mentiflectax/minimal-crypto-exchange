package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger;

    WalletObserver(Logger logger) {
        this.logger = logger;
    }

    public WalletObserver() {
        this(LoggerFactory.getLogger(WalletObserver.class));
    }

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
            kit.wallet().addWatchedAddress(Address.fromString(netParams, exchangeAddress));
            // TODO: Write an automated test for the listener
            kit.wallet().addCoinsReceivedEventListener(new BtcReceivedListener(clojureService, exchangeAddress, netParams));
        }
        catch (final Exception exception) {
            // TODO: Test this
            logger.error("An error occurred while setting up BTC wallet listener", exception);
        }
    }
}
