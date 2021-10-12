docker run -v /local/path:/src \
       ethereum/solc:stable \
       -o target \
       --abi \
       --bin \
       src/contracts/token/ERC20/ERC20.sol


