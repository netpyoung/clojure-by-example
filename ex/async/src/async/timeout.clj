(ns async.timeout)

;; https://gobyexample.com/timeouts


(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go go-loop put! alts! alts!!]])

(def c1 (chan 1))

(go (<! (timeout (* 2 1000)))
    (>! c1 "result 1"))

(let [[v ch] (alts!! [c1 (timeout 1000)])]
  (if v
    (println v)
    (println "timeout 1")))
;>> timeout 1


(def c2 (chan 1))

(go (<! (timeout (* 2 1000)))
    (>! c2 "result 2"))


(let [[v ch] (alts!! [c2 (timeout 3000)])]
  (if v
    (println v)
    (println "timeout 2")))
;>> result 2
