pragma solidity ^0.8.0;

import "./ERC20.sol";

contract USDT is ERC20 {
    constructor(uint256 initialSupply) ERC20("Test Tether", "tUSDT") {
    	_mint(msg.sender, initialSupply);
    }
}
