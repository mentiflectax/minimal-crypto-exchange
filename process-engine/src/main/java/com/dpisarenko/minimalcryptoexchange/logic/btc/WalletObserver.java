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
import java.util.Optional;

@Component
public class WalletObserver {
    @Autowired
    ClojureService clojureService;

    @Value("${accounts.btc.exchange.address}")
    String exchangeAddress;

    @PostConstruct
    public void init() {
        //final String exchangeAddress = this.exchangeAddress;
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
                Optional<LogicalTransactionOutput> relevantTxOutput = tx.getOutputs()
                        .stream()
                        .map(output -> new LogicalTransactionOutput()
                                .withTargetAddress(output.getScriptPubKey().getToAddress(netParams))
                                .withAmount(output.getValue()))
                        .filter(logicalTransactionOutput -> exchangeAddress.equals(logicalTransactionOutput.getTargetAddress().toString()))
                        .findFirst();

                if (relevantTxOutput.isPresent()) {
                    Coin amount = relevantTxOutput.get().getAmount();
                    System.out.println(String.format("Received %s BTC", amount.toFriendlyString()));
                    // TODO: Add comments here (to the transaction)
                    // TODO: Adapt this call
                    clojureService.btcTxReceived(wallet, tx, prevBalance, newBalance);
                }
                System.out.println("---");
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
