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
