(ns com.dpisarenko.core)

(defonce state
         (atom {
                :txs []
                }))

; Macros (start)
(defmacro log-info
  [msg]
  (list '. 'logger 'info msg)
  )

(defmacro now
  []
  (list 'new 'java.util.Date))
; Macros (end)

; Camunda stuff (start)
; Camunda stuff (end)

(defn init
  [logger]
  (let [cur-time (now)]
    (defonce logger logger)
    (swap! state assoc :start-time cur-time)
    (log-info "Clojure subsystem started.")
    ))

(defn create-btc-tx
  [tx-id amount]
  (let []
    {
     :currency   "BTC"
     :created-at (now)
     :tx-id tx-id
     :amount amount
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

(defn tx-present?
  [state tx-id currency]
  false)

;; Various functions (end)

;; Delegates (start)

(defn check-btc-arrived
  [del-ex]
  (let [
        tx-id (.getVariable del-ex "INCOMING_TX_ID")
        btc-arrived (tx-present?
                      state
                      tx-id
                      "BTC")
        ]
    ;; TODO: Update the "btc arrived" process variable
    (log-info (str "check-btc-arrived: INCOMING_TX_ID: "
                   tx-id))
    ))

;; Delegates (end)
