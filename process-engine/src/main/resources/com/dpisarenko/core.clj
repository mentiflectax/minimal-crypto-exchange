(ns com.dpisarenko.core
  (:import
    (org.slf4j Logger)
    (java.util Date)
    )
  )

(defonce state {})

(defn init
  [logger]
  (let [
        cur-time (new java.util.Date)
        new-state (assoc state :start-time cur-time)
        ]
    (. logger info "Hello")
    (. logger info (str "cur-time: "
                        cur-time))
    (. logger info (str "Old state: "
                        state))
    (. logger info (str "New state: "
                        new-state))

    ))
