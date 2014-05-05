(ns common.collection-fn)


(def strs ["peach", "apple", "pear", "plum"])

(.indexOf strs "pear")
;=> 2

(some #{"grape"} strs)
;=> nil


(->> strs
     (some (fn [v]
             (re-find #"^p\w*" v))))
;=> "peach"


(->> strs
     (every? (fn [v]
               (re-find #"^p\w*" v))))
;=> false


(->> strs
     (filter (fn [v]
               (some #{\e} v))))
;=> ("peach" "apple" "pear")


(map clojure.string/upper-case strs)
;=> ("PEACH" "APPLE" "PEAR" "PLUM")
