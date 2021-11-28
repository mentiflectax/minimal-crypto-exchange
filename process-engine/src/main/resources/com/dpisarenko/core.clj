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
        ]
    (. logger info "Hello")
    (. logger info (str "cur-time: "
                        cur-time))
    (. logger info (str "Old state: "
                        state))

    ))
