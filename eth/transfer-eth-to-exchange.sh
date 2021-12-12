curl -X POST http://localhost:8178 \
    -H "Content-Type: application/json" \
    --data '{"jsonrpc":"2.0","method":"eth_sendTransaction","params":[{"from": "0x411167FeFecAD12Da17F9063143706C39528aa28", "to": "0x2096e5c460c83dde11a42d7d99b74445fbfa0b2e", "value": "0x288232983","password":"carsdrivefasterbecausetheyhavebrakes"}],"id":1}'
