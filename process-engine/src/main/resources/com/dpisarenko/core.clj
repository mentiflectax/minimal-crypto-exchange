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
  [state address amt currency]
  false)

;; Various functions (end)

;; Delegates (start)

(defn check-btc-arrived
  [del-ex]
  (let [
       exp-source-address nil ;; TODO: Read from the process variables
       exp-amount nil ;; TODO: Read from the process variables
        btc-arrived (tx-present?
                      state
                      exp-source-address
                      exp-amount
                      "BTC")
        ]
    ;; TODO: Update the "btc arrived" process variable
    (log-info "check-btc-arrived")
    ))

;; Delegates (end)
