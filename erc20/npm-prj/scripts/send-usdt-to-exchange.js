async function main() {
    const USDT = await ethers.getContractFactory("USDT");
    const usdtAddress = "0xB816192c15160a2C1B4D032CDd7B1009583b21AF";
    const usdt = await USDT.attach(usdtAddress);
    const amount = 1;
//    const gasLimit = 1000000;
    const exchange = "0x190FD61ED8fE0067f0f09EA992C1BF96209bab66";
    const usdtSender = "0xDd1e8cC92AF9748193459ADDF910E1b96E88154D";

    console.log("Determining the ETH balance of USDT sender...");
    const ethBalance = await usdt.provider.getBalance(usdtSender);
    console.log("ETH balance:", ethBalance);
    console.log("Done");
    
    console.log("Approving the transfer...");
    const approveResult = await usdt.approve(usdtSender, amount+1);
    console.log("Done, result: ", approveResult);
//    await usdt.allowance
    console.log("Sending USDT...");
    const result = await usdt.transferFrom(usdtSender, exchange, amount, { gasLimit: 1000000 });
    console.log("Done, result=", result);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
