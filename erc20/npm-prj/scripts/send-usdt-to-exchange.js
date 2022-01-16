async function main() {
    const USDT = await ethers.getContractFactory("USDT");
    const usdtAddress = "0xa682a5972D1A8175E2431B26586F486bBa161A11";
    const usdt = await USDT.attach(usdtAddress);
    const amount = ethers.BigNumber.from('3500000000000000000');
    const exchange = "0x190FD61ED8fE0067f0f09EA992C1BF96209bab66";
    const usdtSender = "0xDd1e8cC92AF9748193459ADDF910E1b96E88154D";

    console.log("Determining the ETH balance of USDT sender...");
    const ethBalance = await usdt.provider.getBalance(usdtSender);
    console.log("ETH balance:", ethBalance);
    console.log("Done");

    console.log("Determining USDT balance of the exchange...");
    const usdtBalance = await usdt.balanceOf(exchange);
    console.log("Done");
    console.log("USDT balance of the exchange", usdtBalance);
    
    console.log("Approving the transfer...");
    const approveResult = await usdt.approve(usdtSender, amount+1);
    console.log("Done, result: ", approveResult);
    console.log("Sending USDT...");
    const result = await usdt.transferFromWithoutChangingAllowance(usdtSender, exchange, amount, { gasLimit: 1000000 });
    console.log("Done, result=", result);

    console.log("Determining USDT balance of the exchange...");
    const usdtBalance2 = await usdt.balanceOf(exchange);
    console.log("Done");
    console.log("USDT balance of the exchange", usdtBalance2);

}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
