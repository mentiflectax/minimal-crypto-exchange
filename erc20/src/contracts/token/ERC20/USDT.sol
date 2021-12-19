pragma solidity ^0.8.0;

contract ERC20FixedSupply is ERC20 {
    constructor(uint256 initialSupply) public ERC20("Test Tether", "tUSDT") {
    	_mint(msg.sender, initialSupply);
    }
}
