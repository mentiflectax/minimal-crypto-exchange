docker run -v /Users/dp118m/dev/misc/minimal-crypto-exchange/erc20:/sources \
       ethereum/solc:stable \
       -o /sources/target \
       --abi \
       --bin \
       --overwrite \
       /sources/src/contracts/token/ERC20/ERC20.sol


