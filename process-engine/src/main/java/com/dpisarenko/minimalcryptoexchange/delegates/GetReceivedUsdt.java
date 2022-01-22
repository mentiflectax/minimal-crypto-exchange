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

package com.dpisarenko.minimalcryptoexchange.delegates;

import com.dpisarenko.minimalcryptoexchange.logic.eth.CreateWeb3j;
import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20Contract;
import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component("GetReceivedUsdt")
public class GetReceivedUsdt implements JavaDelegate {
    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    private final Function<LoadErc20ContractInput, ERC20> loadErc20Contract;

    private final Function<String, Web3j> createWeb3j;

    GetReceivedUsdt(Function<LoadErc20ContractInput, ERC20> loadErc20Contract, Function<String, Web3j> createWeb3j) {
        this.loadErc20Contract = loadErc20Contract;
        this.createWeb3j = createWeb3j;
    }

    public GetReceivedUsdt() {
        this(new LoadErc20Contract(), new CreateWeb3j());
    }

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        final ERC20 usdtContract = loadErc20Contract.apply(
                new LoadErc20ContractInput()
                        .withPrivateKey(privateKey)
                        .withUsdtContractAddress(usdtContractAddress)
                        .withEthNetworkUrl(ethNetworkUrl));
        final String incomingTxId = (String) delEx.getVariable("INCOMING_TX_ID");

        final Web3j web3 = createWeb3j.apply(ethNetworkUrl);

        final Optional<TransactionReceipt> transactionReceiptOpt = web3.ethGetTransactionReceipt(incomingTxId).send().getTransactionReceipt();

        if (!transactionReceiptOpt.isPresent()) {
            // TODO: Test this
            throw new RuntimeException("No transaction receipt found");
        }

        final TransactionReceipt transactionReceipt = transactionReceiptOpt.get();

        final List<ERC20.TransferEventResponse> transferEvents = usdtContract.getTransferEvents(transactionReceipt);

        if (transferEvents.size() < 1) {
            // TODO: Test this
            throw new RuntimeException("No transfer events found");
        }

        final ERC20.TransferEventResponse transferEvent = transferEvents.get(0);

        final BigInteger usdtReceived = transferEvent._value;

        delEx.setVariable("USDT_RECEIVED", usdtReceived);
        // TODO: Test this
    }
}
