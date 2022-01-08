(ns com.dpisarenko.core
  (:require [com.dpisarenko.common :refer :all])
  )

(def SATOSHI_TO_USD_CONVERSION_FACTOR 0.0004917)

(defonce state
         (atom {
                :txs []
                }))

(defn init
  [logger]
  (let [cur-time (now)]
    (defonce logger logger)
    (swap! state assoc :start-time cur-time)
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
; TODO: Write automated test for check-btc-arrived
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

; TODO: Write automated test for check_max_number_of_wait_cycles_exceeded
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

; TODO: Write automated test for get_received_satoshis
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

; TODO: Write automated test for calculate_usd_amount
(defn calculate_usd_amount
  [de]
  (let [
        sats (.getVariable de "RECEIVED_SATOSHIS")
        usd (* sats SATOSHI_TO_USD_CONVERSION_FACTOR)
        ]
    (.setVariable de "USD_AMOUNT" usd)))

; TODO: Write automated test for check_if_or_we_have_any_usdt
(defn check_if_or_we_have_any_usdt
  [de]
  (let [available-usdt (.getVariable de "USDT_EXCHANGE_BALANCE")]
    (.setVariable de "ANY_USDT_AVAILABLE" (> available-usdt 0))))

; TODO: Write automated test check_if_or_we_have_any_eth
(defn check_if_or_we_have_any_eth
  [de]
  (let [available-eth (.getVariable de "EXCHANGE_ACCOUNT_BALANCE_WEI")]
    (.setVariable de "ANY_ETH_AVAILABLE" (> available-eth 0)))
)

; Camunda stuff (end)
;; Low-level functions (start)
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
