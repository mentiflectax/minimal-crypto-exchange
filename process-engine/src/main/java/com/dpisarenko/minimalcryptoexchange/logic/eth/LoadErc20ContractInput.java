package com.dpisarenko.minimalcryptoexchange.logic.eth;

public class LoadErc20ContractInput {
    private String ethNetworkUrl;

    public LoadErc20ContractInput withEthNetworkUrl(final String ethNetworkUrl) {
        this.ethNetworkUrl = ethNetworkUrl;
        return this;
    }

    public String getEthNetworkUrl() {
        return ethNetworkUrl;
    }
}
