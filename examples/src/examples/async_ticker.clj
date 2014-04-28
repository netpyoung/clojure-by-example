(ns examples.async-ticker)

(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go go-loop put! close! alts! alts!!]])


(defprotocol ITicker
  (start! [this])
  (tick! [this])
  (stop! [this]))


(defrecord Ticker [c msec]
  ITicker
  (start! [this]
    (go-loop []
      (<! (timeout msec))
      (when (>! c (System/currentTimeMillis))
        (recur))))

  (tick! [this]
    (<!! c))

  (stop! [this]
    (close! c)))


(defn ticker [msec]
  (let [c (chan 1)]
    (Ticker. c msec)))


(let [t (ticker 500)]
  (start! t)

  (go-loop []
    (when-let [msec (tick! t)]
      (println msec)
      (recur)))
  (<!! (timeout 2600))
  (println "time over")

  (stop! t))
;>> 1398643673495
;>> 1398643673996
;>> 1398643674496
;>> 1398643674997
;>> 1398643675498
;>> time over
;=> nil





(defmacro with-ticker [[name new] & body]
  `(let [~name ~new]
     (start! ~name)
     (do ~@body)
     (stop! ~name)))


(with-ticker [a (ticker 500)]
  (go-loop []
    (when-let [msec (tick! a)]
      (println msec)
      (recur)))
  (<!! (timeout 2600))
  (println "time over"))
;>> 1398643706297
;>> 1398643706797
;>> 1398643707298
;>> 1398643707798
;>> 1398643708299
;>> time over
;=> nil
