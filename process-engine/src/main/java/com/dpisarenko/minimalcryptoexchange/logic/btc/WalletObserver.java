package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet;
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
        BriefLogFormatter.init();
        final LocalTestNetParams netParams = new LocalTestNetParams()
                .withPort(18444);
        final WalletAppKit kit = createWalletAppKit(netParams);
        kit.connectToLocalHost();
        startAsync(kit);
        awaitRunning(kit);
        final Wallet wallet = kit.wallet();

        wallet.addWatchedAddress(Address.fromString(netParams, exchangeAddress));
        try {
            wallet.addCoinsReceivedEventListener(createBtcReceivedListener(netParams));
        } catch (final Exception exception) {
            // TODO: Test this
            logger.error("An error occurred while setting up BTC wallet listener", exception);
        }
    }

    BtcReceivedListener createBtcReceivedListener(final LocalTestNetParams netParams) {
        return new BtcReceivedListener(clojureService, exchangeAddress, netParams);
    }

    void awaitRunning(WalletAppKit kit) {
        kit.awaitRunning();
    }

    void startAsync(WalletAppKit kit) {
        kit.startAsync();
    }

    WalletAppKit createWalletAppKit(LocalTestNetParams netParams) {
        return new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
    }
}
