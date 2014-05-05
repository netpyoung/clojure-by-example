(ns async.alts)

;; https://gobyexample.com/select

(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go go-loop put! alts! alts!!]])


(def c1 (chan))
(def c2 (chan))

(go (<! (timeout 1000))
    (>! c1 "one"))

(go (<! (timeout (* 2 1000)))
    (>! c2 "two"))

(dotimes [i 2]
  (let [[v ch] (alts!! [c1 c2])]
    (condp = ch
      c1 (println "received" v)
      c2 (println "received" v)
      nil)))
