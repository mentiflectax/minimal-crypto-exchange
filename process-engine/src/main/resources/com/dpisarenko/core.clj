(ns com.dpisarenko.core
  (:import
    (org.slf4j Logger)
    )
  )

(defonce state {})

(defn init
  [logger]
  (let []
    (. logger info "Hello")
    ))
