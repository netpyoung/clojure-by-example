(ns examples.channel-synchronization)

(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go put!]])


;; https://gobyexample.com/channel-synchronization

(defn worker [done-chan]
  (println "working...")
  (timeout 1000)
  (println "done")
  (put! done-chan true))


(def done (chan 1))
(go (worker done))
;=> #<ManyToManyChannel clojure.core.async.impl.channels.ManyToManyChannel@5be88c45>
;>> working...
;>> done



(println (<!! done))
;>> true
;=> nil
