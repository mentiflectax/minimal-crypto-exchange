/*
 * Copyright 2021, 2022 Dmitrii Pisarenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigDecimal;

@Component
public class WalletObserver {
    final static long ONE_MILLION = 1000000L;
    final static BigDecimal SATOSHIS_IN_BITCOIN = BigDecimal.valueOf(100L * ONE_MILLION);

    @Autowired
    ClojureService clojureService;

    @Value("${accounts.btc.exchange.address}")
    String exchangeAddress;

    private final Logger logger;
    WalletAppKit kit;
    private LocalTestNetParams netParams;

    WalletObserver(Logger logger) {
        this.logger = logger;
    }

    public WalletObserver() {
        this(LoggerFactory.getLogger(WalletObserver.class));
    }

    @PostConstruct
    public void init() {
        BriefLogFormatter.init();
        netParams = new LocalTestNetParams()
                .withPort(18444);
        kit = createWalletAppKit(netParams);
        kit.connectToLocalHost();
        startAsync(kit);
        awaitRunning(kit);
        final Wallet wallet = kit.wallet();

        wallet.addWatchedAddress(Address.fromString(netParams, exchangeAddress));
        wallet.addCoinsReceivedEventListener(createBtcReceivedListener(netParams));
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
        final WalletAppKit wak = new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
        DeterministicSeed seed = null;
        wak.restoreWalletFromSeed(seed);
        return wak;
    }
}
