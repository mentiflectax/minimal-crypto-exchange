pragma solidity ^0.8.0;

import "./tokens/ERC20/ERC20.sol";

contract USDT is ERC20 {
    constructor(address owner) ERC20("Test Tether", "tUSDT") {
    	_mint(owner, 1000000000000000000000);
    }
}
