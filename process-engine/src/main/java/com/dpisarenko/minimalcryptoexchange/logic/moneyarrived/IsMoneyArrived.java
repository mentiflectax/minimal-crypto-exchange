package com.dpisarenko.minimalcryptoexchange.logic.moneyarrived;

import com.dpisarenko.minimalcryptoexchange.common.Currency;
import org.bitcoinj.kits.WalletAppKit;

import java.io.File;
import java.util.function.Function;

public class IsMoneyArrived implements Function<IsMoneyArrivedInput, IsMoneyArrivedResult> {
    @Override
    public IsMoneyArrivedResult apply(final IsMoneyArrivedInput input) {
        if (!Currency.BTC.equals(input.getCurrency())) {
            throw new IllegalArgumentException("Currently only BTC is supported");
        }
/*
        try {
            //initialize files and stuff here
            WalletAppKit kit = new WalletAppKit(input.getNetworkParameters(), new File("."), "_minimalCryptoExchangeBtcWallet");
            kit.setAutoSave(true);
            kit.setAutoSave(true);
            kit.connectToLocalHost();
            kit.startAsync();
            kit.awaitRunning();
            kit.peerGroup().addPeerDiscovery(new DnsDiscovery(params));
            kit.wallet().addWatchedAddress(new Address(params, "1NxxxxxxxxxxxxxxxxC4"));
            kit.wallet().addEventListener(new AbstractWalletEventListener() {
                @Override
                public synchronized void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
                    System.out.println("[main]: COINS RECIEVED!");
                    System.out.println("\nReceived tx " + tx.getHashAsString());
                    System.out.println(tx.toString());
                }
            });
        } catch (IOException e) {

            e.printStackTrace();
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
*/
        return new IsMoneyArrivedResult();
    }
}
