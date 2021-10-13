docker run -v .:/sources \
       ethereum/solc:stable \
       -o /sources/target \
       --abi \
       --bin \
       /sources/src/contracts/token/ERC20/ERC20.sol


