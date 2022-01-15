async function main() {
    const USDT = await ethers.getContractFactory("USDT");
    const usdt = await USDT.attach(
  "0xB816192c15160a2C1B4D032CDd7B1009583b21AF"
    );
    const usdtSender = "0xDd1e8cC92AF9748193459ADDF910E1b96E88154D";

    console.log("Getting balance of the USDT sender...");
    const balance = await.balanceOf(usdtSender);
    console.log("Done");
    console.log("Balance: ", balance);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
