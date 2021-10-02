curl -X POST http://localhost:8178 \
    -H "Content-Type: application/json" \
   --data '{"jsonrpc":"2.0", "method":"eth_getBalance", "params":["0x411167FeFecAD12Da17F9063143706C39528aa28","latest"], "id":1}'
