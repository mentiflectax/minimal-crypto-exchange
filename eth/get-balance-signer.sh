curl -X POST http://localhost:8178 \
    -H "Content-Type: application/json" \
   --data '{"jsonrpc":"2.0", "method":"eth_getBalance", "params":["0x0c56352F05De44C9b5BA8bcF9BDEc7e654993339,"latest"], "id":1}'
