(ns examples.const)

;; http://stackoverflow.com/questions/9162558/how-does-clojure-const-work

(def ^:const s "constant")

(def ^:const n 500000000)

(def ^:const d (/ 3e20 n))

(println s)
;>> constant
;=> nil

(println d)
;>> 6.0E11
;=> nil


(println (long d))
;>> 600000000000
;=> nil


(println (Math/sin n))
;>> -0.28470407323754404
;=> nil
