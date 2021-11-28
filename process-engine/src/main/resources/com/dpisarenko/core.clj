(ns com.dpisarenko.core)

(defonce state (atom {}))

; Macros (start)
(defmacro log-info
  [msg]
  (list '. 'logger 'info msg)
  )
; Macros (end)

(defn init
  [logger]
  (let [
        cur-time (new java.util.Date)
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

(defn btcTxReceived
  [wallet
   tx
   prev-balance
   new-balance]
  (log-info (str "state: " @state))
  (log-info (str "btcTxReceived"))
  )