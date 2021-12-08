package com.dpisarenko.minimalcryptoexchange.logic.btc;

import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.TransactionOutput;
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

        try {
            final WalletAppKit kit = new WalletAppKit(netParams, new File("."), "_minimalCryptoExchangeBtcWallet");
            kit.connectToLocalHost();
            kit.startAsync();
            kit.awaitRunning();

            kit.wallet().addWatchedAddress(Address.fromString(netParams,  exchangeAddress));

            kit.wallet().addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
                // tx.getOutputs().get(0).getValue()
                System.out.println("---");
                for (final TransactionOutput output : tx.getOutputs()) {
                    final Address targetAddress = output.getScriptPubKey().getToAddress(netParams);
                    Coin amount = output.getValue();

                    System.out.println(String.format("Recipient '%s', amount '%s'",
                            targetAddress, amount.toFriendlyString()));
                }
                System.out.println("---");

                clojureService.btcTxReceived(wallet, tx, prevBalance, newBalance);
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
