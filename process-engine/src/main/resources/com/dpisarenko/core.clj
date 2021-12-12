(ns com.dpisarenko.core
  (:require [com.dpisarenko.common :refer :all])
  (:require [com.dpisarenko.detect-btc-reception-process :refer :all])
  (:require [com.dpisarenko.exchange-btc-for-usdt-process :refer :all])
  )

(defonce state
         (atom {
                :txs []
                }))


; Camunda stuff (start)
; Camunda stuff (end)

(defn init
  [logger]
  (let [cur-time (now)]
    (defonce logger logger)
    (swap! state assoc :start-time cur-time)
    (log-info "Clojure subsystem started.")
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
