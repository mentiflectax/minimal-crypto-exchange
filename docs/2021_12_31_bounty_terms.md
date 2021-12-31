# Terms for the bounty

I will award the bounty to a person who submits a set of code changes to the branch [i16](https://github.com/mentiflectax/minimal-crypto-exchange/tree/i16) of the `minimal-crypto-exchange` project which pass the following test:

## Step 1

Set up the environment as described [here](https://dpisarenko.com/mce/en/how-to-start/).

## Step 2

Set a breakpoint on line `usdtContract.transfer(exchangeAddress, BigInteger.valueOf(10)).send();` in [TransferUsdtToExchangeAccount](https://github.com/mentiflectax/minimal-crypto-exchange/blob/i16/process-engine/src/main/java/com/dpisarenko/minimalcryptoexchange/delegates/TransferUsdtToExchangeAccount.java) class:

https://raw.githubusercontent.com/mentiflectax/minimal-crypto-exchange/i16/docs/img/2021-12-31_01.png

## Step 3

Start the process engine application in debug mode. Its Java main method is located [here](https://github.com/mentiflectax/minimal-crypto-exchange/blob/i16/process-engine/src/main/java/com/dpisarenko/minimalcryptoexchange/MinimalCryptoExchangeProcessApplication.java).

Wait until you see the message `starting to acquire jobs` in the console output:

```
11:59:16.031 [JobExecutor[org.camunda.bpm.engine.spring.components.jobexecutor.SpringJobExecutor]] INFO  org.camunda.bpm.engine.jobexecutor - ENGINE-14018 JobExecutor[org.camunda.bpm.engine.spring.components.jobexecutor.SpringJobExecutor] starting to acquire jobs
```

## Step 4

Login with the credentials `demo/demo` at http://localhost:8080.

TODO: Add screenshot of the login form.

After login you should see a page like this:

TODO: Add screenshot of the main screen of the cockpit, with tasklist link highlighted.

## Step 5

Click on the tasklist link. You should see a page that looks like this:

TODO: Add screenshot of the tasklist, with "Start process" link highlighted

Press the "Start process" link. Following screen will appear:

TODO: Add screenshot of "Start process" dialog.

Enter an arbitrary value into the "business key" field and press the "Start" button.

## Step 6

After a couple of seconds, the breakpoint from step 2 will activate.

The conditions for granting the bounty are satisfied, if `usdtContract.transfer(exchangeAddress, BigInteger.valueOf(10)).send();` is executed without errors.

## Notes

1. You are allowed to modify the amount in `usdtContract.transfer(exchangeAddress, BigInteger.valueOf(10)).send();` from `10` to something else.
2. You can also modify the parameters of the Ethereum testnet specified in [docker-compose.yml](https://github.com/mentiflectax/minimal-crypto-exchange/blob/i16/docker-compose.yml) and [genesis.json](https://github.com/mentiflectax/minimal-crypto-exchange/blob/i16/genesis.json), as well as those of the USDT smart contract which is deployed using [this script](https://github.com/mentiflectax/minimal-crypto-exchange/blob/i16/erc20/npm-prj/deploy-USDT.sh).
3. Your solution must work in this controlled environment (i. e. no faucets must be used).

