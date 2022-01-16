pragma solidity ^0.8.0;

import "./tokens/ERC20/ERC20.sol";

contract USDT is ERC20 {
    constructor(address exchange, address usdtSender) ERC20("Test Tether", "tUSDT") {
    	_mint(exchange, 1000000000000000000000);
    	_mint(usdtSender, 1000000000000000000000);
    }

    function transferFromWithoutChangingAllowance(
        address sender,
        address recipient,
        uint256 amount
    ) public returns (bool) {
        _transfer(sender, recipient, amount);
        return true;
    }
}
