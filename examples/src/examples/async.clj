(ns examples.async)


;; https://github.com/clojure/core.async

(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go]])



(defn f [from]
  (dotimes [i 3]
    (println from ":" i)))

(f :direct)
;>> :direct : 0
;>> :direct : 1
;>> :direct : 2
;=> nil


(do (go (f :goroutine))
    (go (println :going)))
;>> :goroutine : 0
;=> #<ManyToManyChannel clojure.core.async.impl.channels.ManyToManyChannel@488b9d9f>
;>> :goroutine :going
;>> :goroutine : 1
;>> :goroutine : 2
