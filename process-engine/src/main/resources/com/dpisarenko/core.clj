(ns com.dpisarenko.core
  (:require [com.dpisarenko.common :refer :all])
  )

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
                   tx-id ", BTC arrived: " btc-arrived))
    ))

(defn set_retry_counter_to_0
  [de]
  (.setVariable de "RETRY_COUNTER" 0)
  )

(defn increment_wait_counter
  [de]
  (let [
        old-value (.getVariable de "RETRY_COUNTER")
        new-value (+ 1 old-value)
        ]
    (.setVariable de "RETRY_COUNTER" new-value)
    )
  )

(defn check_max_number_of_wait_cycles_exceeded
  [de]
  (let [
        retries (.getVariable de "RETRY_COUNTER")
        max-retries 5
        max-retries-exceeded (> retries max-retries)
        ]
    (.setVariable de
                  "MAX_NUMBER_OF_WAITING_CYCLES_EXCEEDED"
                  max-retries-exceeded)
    )
  )

(declare find-tx)

(defn get_received_satoshis
  [de]
  (let [
        tx-id (.getVariable de "INCOMING_TX_ID")
        tx (find-tx @state tx-id "BTC")
        amount (:amount tx)
        amount-sats (.getValue amount)
        ]
    (.setVariable de "RECEIVED_SATOSHIS" amount-sats)
    ))

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
    (first matching-txs)
    )
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
    (assoc old-state :txs new-tx-list)
    ))

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
;; Low-level functions (end)
