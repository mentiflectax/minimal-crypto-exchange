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

import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20Contract;
import com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.contracts.eip20.generated.ERC20;

import java.math.BigInteger;
import java.util.function.Function;

@Component("ApproveUsdtTransfer")
public class ApproveUsdtTransfer implements JavaDelegate {
    @Value("${accounts.eth.exchange.private-key}")
    String privateKey;

    @Value("${accounts.eth.usdt.contract-address}")
    String usdtContractAddress;

    @Value("${networks.eth.url}")
    String ethNetworkUrl;

    @Value("${accounts.eth.exchange.address}")
    String exchangeAddress;

    private final Function<LoadErc20ContractInput, ERC20> loadErc20Contract;

    ApproveUsdtTransfer(Function<LoadErc20ContractInput, ERC20> loadErc20Contract) {
        this.loadErc20Contract = loadErc20Contract;
    }

    ApproveUsdtTransfer() {
        this(new LoadErc20Contract());
    }

    @Override
    public void execute(final DelegateExecution delEx) throws Exception {
        final ERC20 usdtContract = loadErc20Contract.apply(
                new LoadErc20ContractInput()
                        .withPrivateKey(privateKey)
                        .withUsdtContractAddress(usdtContractAddress)
                        .withEthNetworkUrl(ethNetworkUrl));
        final BigInteger usdtAmount = (BigInteger) delEx.getVariable("USDT_AMOUNT");
        final BigInteger amountToApprove = usdtAmount.add(BigInteger.ONE);
        usdtContract.approve(exchangeAddress, amountToApprove).send();
    }
}
