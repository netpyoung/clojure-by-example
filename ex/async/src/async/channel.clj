(ns async.channel)


(require '[clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go]])

(def message (chan))

(go (>! message "ping"))
(go (println (<! message)))
;>> ping
;=> #<ManyToManyChannel clojure.core.async.impl.channels.ManyToManyChannel@369c1699>


(go (>! message "ping"))
(println (<!! message))
;>> ping
;=> nil
