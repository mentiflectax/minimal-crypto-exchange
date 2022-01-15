async function main() {
    // TODO: Load the USDT contract by address
    
    const USDT = await ethers.getContractFactory("USDT");
    const usdt = await MyContract.attach(
  "0xB816192c15160a2C1B4D032CDd7B1009583b21AF"
    );
    const amount = 350;
    const exchange = "0x190FD61ED8fE0067f0f09EA992C1BF96209bab66";
    const usdtSender = "0xDd1e8cC92AF9748193459ADDF910E1b96E88154D";
    usdt.approve(usdtSender, amount);
    usdt.transferFrom(usdtSender, exchange, amount);

  console.log("Sending USDT...");


  console.log("USDT contract deployed to:", usdt.address);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
