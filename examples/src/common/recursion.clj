(ns common.recursion)



(defn fact [n]
  (if (zero? n)
    1
    (* n (fact (dec n)))))


(fact 7)
;=> 5040


;; http://stackoverflow.com/questions/1662336/clojure-simple-factorial-causes-stack-overflow
(defn fact [x]
  (loop [n x f 1]
    (if (= n 1)
      f
      (recur (dec n) (* f n)))))

(fact 7)
;=> 5040
