(ns examples.atomic)

(require '[clojure.core.async :as async :refer [<! >! <!! >!! timeout chan alt! go go-loop put! pipe close! map<]])

(do
  (def ops* (atom 0))

  (dotimes [i 50]
    (go (swap! ops* inc))
    )

  (<!! (timeout 1000))

  (println @ops*))

;>> 50
;=> nil
