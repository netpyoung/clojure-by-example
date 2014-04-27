(ns examples.values)

;; Strings, which can be added together with `str`.
(println (str "clojure" "lang"))
;>> clojurelang
;=> nil


;; Integers and floats.
(println "1+1 =" (+ 1 1))
;>> 1+1 = 2
;=> nil

(println "7.0/3.0 =" (/ 7.0 3.0))
;>> 7.0/3.0 = 2.3333333333333335
;=> nil


;; Booleans, with boolean operators as you'd expect.
(println (and true false))
;>> false
;=> nil

(println (or true false))
;>> true
;=> nil

(println (not true))
;>> false
;=> nil
