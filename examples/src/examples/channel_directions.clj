(ns examples.channel-directions)

(require '[clojure.core.async :as async :refer [<! >! <!! >!! timeout chan alt! go go-loop put!]])

(defn ping [pings msg]
  (put! pings msg))

(defn pong [pings pongs]
  (->> (<!! pings)
       (put! pongs)))


(def pi (chan 1))
(def po (chan 1))

(ping pi "passed message")
(pong pi po)

(println (<!! po))
;>> passed message
;=> nil


;;
;;
;;

(defn ping' [pings msg]
  (go
    (>! pings msg)))

(defn pong' [pings pongs]
  (go
    (->> (<! pings)
         (>! pongs))))


(def pi' (chan 1))
(def po' (chan 1))

(ping pi' "passed message")
(pong pi' po')

(println (<!! po'))
;>> passed message
;=> nil
