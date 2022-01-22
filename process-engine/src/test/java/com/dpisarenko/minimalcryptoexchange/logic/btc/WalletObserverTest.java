/*
 * Copyright 2021, 2022 Dmitrii Pisarenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dpisarenko.minimalcryptoexchange.logic.btc;

import org.apache.commons.lang3.ArrayUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.Wallet;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.slf4j.Logger;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class WalletObserverTest {
    static final String EXCHANGE_ADDRESS = "2N1akcbAa3oXJ7RNxrAWmL7ZuuU9iHg9tpG";
    public static final LocalTestNetParams NET_PARAMS = new LocalTestNetParams()
            .withPort(18444);

    @Test
    public void givenConfiguration_whenInit_thenSetupCoinsReceivedListener() {
        // Given
        final Logger logger = mock(Logger.class);
        final WalletObserver sut = spy(new WalletObserver(logger));
        sut.exchangeAddress = EXCHANGE_ADDRESS;
        final WalletAppKit kit = mock(WalletAppKit.class);
        doAnswer(iom -> {
            final LocalTestNetParams netParams = iom.getArgument(0);
            assertEquals(18444, netParams.getPort());
            return kit;
        }).when(sut).createWalletAppKit(any());

        final Wallet wallet = mock(Wallet.class);
        when(kit.wallet()).thenReturn(wallet);

        doNothing().when(sut).startAsync(kit);
        doNothing().when(sut).awaitRunning(kit);

        final BtcReceivedListener btcReceivedListener = mock(BtcReceivedListener.class);
        doReturn(btcReceivedListener).when(sut).createBtcReceivedListener(NET_PARAMS);

        // When
        sut.init();

        // Then
        verify(sut).init();
        verify(sut).createWalletAppKit(any());
        verify(kit).connectToLocalHost();
        verify(sut).startAsync(kit);
        verify(sut).awaitRunning(kit);
        verify(kit).wallet();
        verify(wallet).addWatchedAddress(Address.fromString(NET_PARAMS, EXCHANGE_ADDRESS));
        verify(sut).createBtcReceivedListener(NET_PARAMS);
        verify(wallet).addCoinsReceivedEventListener(btcReceivedListener);
        verifyNoMoreInteractions(sut, kit, logger);
    }
}