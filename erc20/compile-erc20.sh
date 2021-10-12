docker run -v /local/path:/sources \
       ethereum/solc:stable \
       -o /sources/output \
       --abi \
       --bin \
       /sources/Contract.sol

