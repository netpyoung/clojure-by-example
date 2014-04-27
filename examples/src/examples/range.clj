(ns examples.range)

(def nums [2 3 4])

(reduce + nums)
;=> 9


(doseq [[i num] (map-indexed vector nums)]
  (when (= num 3)
    (println "index:" i)))
;>> index: 1
;=> nil


(def kvs {"a" "apple",
          "b" "banana"})

(doseq [[k v] kvs]
  (printf "%s -> %s\n" k v))
;>> a -> apple
;>> b -> banana
;=> nil


(doseq [[i c] (map-indexed vector "clojure")]
  (println i c))
;>> 0 c
;>> 1 l
;>> 2 o
;>> 3 j
;>> 4 u
;>> 5 r
;>> 6 e
;=> nil


(range 10)
;=> (0 1 2 3 4 5 6 7 8 9)

(range 2 10 3)
;=> (2 5 8)

(range 10 2 -1)
;=> (10 9 8 7 6 5 4 3)

(range 10 11 -1)
;=> ()
