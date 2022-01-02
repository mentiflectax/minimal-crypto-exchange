async function main() {
  // We get the contract to deploy
    const USDT = await ethers.getContractFactory("USDT");
    // TODO: Add here the exchange address which should
    // have all the tUSDT
    const usdt = await USDT.deploy("0x190FD61ED8fE0067f0f09EA992C1BF96209bab66", 1000000000000000);

  console.log("USDT contract deployed to:", usdt.address);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
