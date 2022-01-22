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

package com.dpisarenko.minimalcryptoexchange.logic.eth;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LoadErc20ContractInput {
    private String ethNetworkUrl;
    private String privateKey;
    private String usdtContractAddress;

    public LoadErc20ContractInput withEthNetworkUrl(final String ethNetworkUrl) {
        this.ethNetworkUrl = ethNetworkUrl;
        return this;
    }

    public LoadErc20ContractInput withPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public LoadErc20ContractInput withUsdtContractAddress(final String address) {
        this.usdtContractAddress = address;
        return this;
    }

    public String getEthNetworkUrl() {
        return ethNetworkUrl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getUsdtContractAddress() {
        return usdtContractAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LoadErc20ContractInput that = (LoadErc20ContractInput) o;

        return new EqualsBuilder().append(ethNetworkUrl, that.ethNetworkUrl).append(privateKey, that.privateKey).append(usdtContractAddress, that.usdtContractAddress).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(ethNetworkUrl).append(privateKey).append(usdtContractAddress).toHashCode();
    }
}
