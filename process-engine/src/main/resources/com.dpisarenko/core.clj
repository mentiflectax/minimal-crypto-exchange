(ns com.dpisarenko.core)

(defonce state {})

(defn init
  [logger]
  (let []
    (. logger info "Hello")
    ))
