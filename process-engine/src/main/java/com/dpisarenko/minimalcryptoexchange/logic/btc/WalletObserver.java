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
import org.apache.commons.lang3.ArrayUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Protos;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bouncycastle.util.encoders.Hex;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

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

    public void sendBtc(BigDecimal btcAmount, String targetBtcAddress) {
        byte[] privBytes = Hex.decode("ef235aacf90d9f4aadd8c92e4b2562e1d9eb97f0df9ba3b508258739cb013db2");
        byte[] appendZeroByte = ArrayUtils.addAll(new byte[1], privBytes);
        final ECKey ecKey = ECKey.fromPrivate(new BigInteger(appendZeroByte), false);

        final Wallet sourceWallet = Wallet.fromKeys(netParams, Collections.singletonList(ecKey));
        sourceWallet.upgradeToDeterministic(null);

        final BigDecimal satoshisToSend = btcAmount.multiply(SATOSHIS_IN_BITCOIN);
        final Coin coinToSend = Coin.valueOf(satoshisToSend.longValue());
        final Address targetAddress = Address.fromString(netParams, targetBtcAddress);

        final Transaction tx = new Transaction(netParams);
        tx.addInput(kit.wallet().getUnspents().get(0));
        tx.addOutput(coinToSend, targetAddress);

        final SendRequest request = SendRequest.forTx(tx);
        request.feePerKb = Coin.valueOf(1000);

        try {
            kit.wallet().sendCoins(request);
        } catch (InsufficientMoneyException exception) {
            throw new BpmnError("INSUFFICIENT_FUNDS");
        }
    }
}
