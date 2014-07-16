(ns common.maps)

;; http://clojuredocs.org/clojure_core/clojure.core/hash-map

(def m (hash-map :k1 1, :k2 2))

(println m)
;>> {:k1 1, :k2 2}
;=> nil


(get m :k1)
;=> 7

(count m)
;=> 2


(def m' (dissoc m :k2))

(println m')
;>> {:k1 1}
;=> nil

(get m' :k2 "not-found")
;=> "not-found"



(def n {"foo" 1
        "bar" 2})

(println n)
;>> {foo 1, bar 2}
;=> nil
