(ns examples.array)

(use '[clojure.pprint])


(def a (make-array Integer/TYPE 5))

(pprint a)
;>> [0, 0, 0, 0, 0]
;=> nil



(do (print "set: ")
    (aset a 4 100)
    (pprint a))
;>> set: [0, 0, 0, 0, 100]
;=> nil


(println "get:" (aget a 4))
;>> get: 100
;=> nil

(println "len:" (count a))
;>> len: 5
;=> nil

(println "alen:" (alength a))
;>> alen: 5
;=> nil


(def b (into-array [1 2 3 4 5]))
;=> #'examples.array/b

(pprint b)
;>> [1, 2, 3, 4, 5]
;=> nil



(def x (make-array Integer/TYPE 2 3))

(pprint x)
;>> [[0, 0, 0], [0, 0, 0]]
;=> nil


(dotimes [i 2]
  (dotimes [j 3]
    (aset x i j (int (+ i j)))))
;=> nil


(pprint x)
;>> [[0, 1, 2], [1, 2, 3]]
;=> nil
