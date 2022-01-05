package com.dpisarenko.minimalcryptoexchange.logic.eth;

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
}
