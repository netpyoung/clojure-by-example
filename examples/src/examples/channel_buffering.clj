(ns examples.channel-buffering)

(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go]])


;; https://gobyexample.com/channel-buffering
(def message (chan 2))

(go (>! message "buffered")
    (>! message "channel"))

(println (<!! message))
;>> buffered
;=> nil

(println (<!! message))
;>> channel
;=> nil
