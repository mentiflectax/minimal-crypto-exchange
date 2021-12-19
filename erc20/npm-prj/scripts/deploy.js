async function main() {
  // We get the contract to deploy
  const USDT = await ethers.getContractFactory("USDT");
  const usdt = await USDT.deploy(1000);

  console.log("USDT contract deployed to:", usdt.address);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
