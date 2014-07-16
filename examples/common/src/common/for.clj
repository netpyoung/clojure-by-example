(ns common.for)

(dotimes [i 3]
  (println i))
;>> 0
;>> 1
;>> 2
;=> nil

(doseq [i (range 7 10)]
  (println i))
;>> 7
;>> 8
;>> 9
;=> nil
