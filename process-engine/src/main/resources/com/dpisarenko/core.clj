(ns com.dpisarenko.core
  (:import
    (org.slf4j Logger)
    (java.util Date)
    )
  )

(defonce state (atom {}))

(defn init
  [logger]
  (let [
        cur-time (new java.util.Date)
        old-state state
        ; new-state (assoc state :start-time cur-time)
        new-state (swap! state assoc :start-time cur-time)
        ;new-state nil
        ]

    (. logger info "Hello")
    (. logger info (str "cur-time: "
                        cur-time))
    (. logger info (str "Old state: "
                        old-state))
    (. logger info (str "New state: " new-state))

    ))
