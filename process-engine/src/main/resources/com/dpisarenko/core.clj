;
; Copyright 2021, 2022 Dmitrii Pisarenko
;
; Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
;
; The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
;
; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
;
;

(ns com.dpisarenko.core
  (:require [com.dpisarenko.common :refer :all])
  )

(def SATOSHI_TO_USD_CONVERSION_FACTOR 0.0004917)

(defonce state
         (atom {
                :eth-private-key       nil
                :eth-network-url       nil
                :usdt-contract-address nil
                :eth-exchange-address  nil
                :txs                   []
                }))

(defn init
  [logger
   eth-network-url
   usdt-contract-address
   eth-exchange-address
   eth-private-key
   ]
  (let [cur-time (now)]
    (defonce logger logger)
    (swap! state assoc
           :start-time cur-time
           :eth-private-key eth-private-key
           :eth-network-url eth-network-url
           :usdt-contract-address usdt-contract-address
           :eth-exchange-address eth-exchange-address
           )
    (log-info "Clojure subsystem started.")
    ))

(declare create-btc-tx)
(declare append-tx)
(defn btcTxReceived
  [tx-id amount]
  (let [
        new-tx (create-btc-tx
                 tx-id
                 amount)
        ]
    (swap! state append-tx new-tx)
    (log-info (str "btcTxReceived"))
    (log-info (str "New state: " @state))
    )
  )

;; Various functions (start)
(declare btcTxReceived)
(declare tx-present?)
;; Various functions (end)

; Camunda stuff (start)
(defn check-btc-arrived
  [de]
  (let [
        tx-id (.getVariable de "INCOMING_TX_ID")
        btc-arrived (tx-present?
                      @state
                      tx-id
                      "BTC")
        ]
    (.setVariable de "BTC_ARRIVED" btc-arrived)
    (log-info (str "check-btc-arrived: check-btc-arrived: INCOMING_TX_ID: "
                   tx-id ", BTC arrived: " btc-arrived))))

(defn set_retry_counter_to_0
  [de]
  (.setVariable de "RETRY_COUNTER" 0))

(defn increment_wait_counter
  [de]
  (let [
        old-value (.getVariable de "RETRY_COUNTER")
        new-value (+ 1 old-value)
        ]
    (.setVariable de "RETRY_COUNTER" new-value)))

(defn check_max_number_of_wait_cycles_exceeded
  [de]
  (let [
        retries (.getVariable de "RETRY_COUNTER")
        max-retries 5
        max-retries-exceeded (> retries max-retries)
        ]
    (.setVariable de
                  "MAX_NUMBER_OF_WAITING_CYCLES_EXCEEDED"
                  max-retries-exceeded)))

(declare find-tx)
(defn get_received_satoshis
  [de]
  (let [
        tx-id (.getVariable de "INCOMING_TX_ID")
        tx (find-tx @state tx-id "BTC")
        amount (:amount tx)
        amount-sats (.getValue amount)
        ]
    (.setVariable de "RECEIVED_SATOSHIS" amount-sats)))

(defn calculate_usd_amount
  [de]
  (let [
        sats (.getVariable de "RECEIVED_SATOSHIS")
        usd (* sats SATOSHI_TO_USD_CONVERSION_FACTOR)
        ]
    (.setVariable de "USD_AMOUNT" usd)))

(defn check_if_or_we_have_any_usdt
  [de]
  (let [available-usdt (.getVariable de "USDT_EXCHANGE_BALANCE")]
    (.setVariable de "ANY_USDT_AVAILABLE" (> available-usdt 0))))

(defn check_if_or_we_have_any_eth
  [de]
  (let [available-eth (.getVariable de "EXCHANGE_ACCOUNT_BALANCE_WEI")]
    (.setVariable de "ANY_ETH_AVAILABLE" (> available-eth 0)))
  )

(declare get_exchange_usdt_balance)

(defn get_old_usdt_balance
  [de]
  (.setVariable de "OLD_USDT_BALANCE" (get_exchange_usdt_balance)))

(defn get_new_usdt_balance
  [de]
  (.setVariable de "NEW_USDT_BALANCE" (get_exchange_usdt_balance)))

(defn convert-usdt-amount-to-usd
  [de]
  (let [
        usdt-amount (.getVariable de "USDT_RECEIVED")
        conversion-factor (new BigInteger com.dpisarenko.minimalcryptoexchange.delegates.ConvertUsdToUsdt.USD_TO_USDT_CONVERSION_FACTOR)
        usd-amount (.divide usdt-amount conversion-factor)
        ]
    (.setVariable de "USD_AMOUNT" usd-amount)))


; Camunda stuff (end)
;; Low-level functions (start)
(defn get_exchange_usdt_balance
  []
  (let [eth-network-url (:eth-network-url @state)
        eth-private-key (:eth-private-key @state)
        usdt-contract-address (:usdt-contract-address @state)
        eth-exchange-address (:eth-exchange-address @state)
        lc-input (-> (new com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20ContractInput)
                     (.withEthNetworkUrl eth-network-url)
                     (.withPrivateKey eth-private-key)
                     (.withUsdtContractAddress usdt-contract-address))
        lc-fn (new com.dpisarenko.minimalcryptoexchange.logic.eth.LoadErc20Contract)
        usdt-contract (.apply lc-fn lc-input)
        balance-of-response (.balanceOf usdt-contract eth-exchange-address)
        usdt-balance-big-integer (.send balance-of-response)
        usdt-balance-long (.longValue usdt-balance-big-integer)
        ]
    usdt-balance-long
    )
  )


(defn create-btc-tx
  [tx-id amount]
  (let []
    {
     :currency   "BTC"
     :created-at (now)
     :tx-id      tx-id
     :amount     amount
     }
    ))

(defn append-tx
  [old-state new-tx]
  (let [
        old-tx-list (get old-state :txs)
        new-tx-list (conj old-tx-list new-tx)
        ]
    (assoc old-state :txs new-tx-list)))

(defn tx-present?
  [state tx-id currency]
  (let [txs (:txs state)
        matching-txs (filter (fn [cur-tx]
                               (let [cur-tx-currency (:currency cur-tx)
                                     cur-tx-id (:tx-id cur-tx)

                                     ]
                                 (and
                                   (= currency cur-tx-currency)
                                   (= tx-id cur-tx-id)))
                               )
                             txs
                             )
        tx-exists (not (empty? matching-txs))
        ]
    tx-exists)
  )

(defn find-tx
  [state tx-id currency]
  (let [
        txs (:txs state)
        matching-txs (filter
                       (fn [cur-tx]
                         (let [cur-tx-currency (:currency cur-tx)
                               cur-tx-id (:tx-id cur-tx)

                               ]
                           (and
                             (= currency cur-tx-currency)
                             (= tx-id cur-tx-id)))
                         )
                       txs
                       )
        ]
    (first matching-txs)))
;; Low-level functions (end)
