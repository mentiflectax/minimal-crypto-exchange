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

(defn init
  [logger]
  (let [
        cur-time (now)
        old-state @state
        new-state (swap! state assoc :start-time cur-time)
        ]
    (defonce logger logger)
    (log-info "Hello")
    (log-info (str "cur-time: "
                   cur-time))
    (log-info (str "Old state: "
                   old-state))
    (log-info (str "New state: " new-state))

    ))

(defn create-btc-tx
  [wallet
   tx
   prev-balance
   new-balance]
  (let []
    {
     :currency   "BTC"
     :created-at (now)
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
  [wallet
   tx
   prev-balance
   new-balance]
  (let [
        new-tx (create-btc-tx
                 wallet
                 tx
                 prev-balance
                 new-balance)
        ]
    (swap! state append-tx new-tx)
    (log-info (str "state: " @state))
    (log-info (str "btcTxReceived"))
    )
  )