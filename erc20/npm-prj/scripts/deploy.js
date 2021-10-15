async function main() {
  // We get the contract to deploy
  const ERC20 = await ethers.getContractFactory("ERC20");
  const erc20 = await ERC20.deploy("Test Tether", "tUSDT");

  console.log("ERC20 deployed to:", erc20.address);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
