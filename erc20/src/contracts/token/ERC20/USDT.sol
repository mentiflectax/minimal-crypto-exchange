pragma solidity ^0.8.0;

contract ERC20FixedSupply is ERC20 {
    constructor() public {
        totalSupply += 1000;
        balances[msg.sender] += 1000;
    }
}
